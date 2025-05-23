package com.backApi.demo.Controller;

import com.backApi.demo.Dto.LoginRequest; // Ya lo tienes
import com.backApi.demo.Dto.AuthResponse; // ¡Nuevo!
import com.backApi.demo.Model.Usuario; // Para obtener datos del usuario
import com.backApi.demo.Service.UsuarioService;
import org.springframework.http.ResponseEntity;
import com.backApi.demo.Dto.ErrorResponseU;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

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
        System.out.println("Contraseña recibida para login: " + loginRequest.getPassword());
        Usuario usuarioLogeado = usuarioService.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));

        String jwtToken = "tu_token_generado_aqui";

        AuthResponse response = new AuthResponse(
                "Login exitoso",
                jwtToken,
                usuarioLogeado.getUserId(),
                usuarioLogeado.getEmail(),
                usuarioLogeado.getNombre(),
                usuarioLogeado.getTelefono(),
                usuarioLogeado.getDireccion()
        );
        return ResponseEntity.ok(response);

    }

}