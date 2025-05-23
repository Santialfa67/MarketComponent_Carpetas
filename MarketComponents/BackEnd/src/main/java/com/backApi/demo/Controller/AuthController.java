package com.backApi.demo.Controller;

import com.backApi.demo.Dto.LoginRequest; // Ya lo tienes
import com.backApi.demo.Dto.AuthResponse; // ¡Nuevo!
import com.backApi.demo.Model.Usuario; // Para obtener datos del usuario
import com.backApi.demo.Service.UsuarioService;
import org.springframework.http.HttpStatus; // Para manejar códigos de estado HTTP
import org.springframework.http.ResponseEntity;
import com.backApi.demo.Dto.ErrorResponseU;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional; // Necesario para buscar usuario

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Ahora este findByEmail llama al método que acabas de añadir en UsuarioService
        Optional<Usuario> usuarioOptional = usuarioService.findByEmail(loginRequest.getEmail());

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (usuario.getPassword().equals(loginRequest.getPassword())) { // ¡Recuerda hashear!
                AuthResponse authResponse = new AuthResponse("Login exitoso", null, usuario.getUserId(), usuario.getEmail());
                return ResponseEntity.ok(authResponse);
            }
        }
        ErrorResponseU errorResponse = new ErrorResponseU("Credenciales incorrectas", LocalDateTime.now().toString(), HttpStatus.UNAUTHORIZED.value());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
}