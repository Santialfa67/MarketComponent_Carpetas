package com.backApi.demo.Controller;

import com.backApi.demo.Model.Usuario;
import com.backApi.demo.Service.UsuarioService;
import com.backApi.demo.Dto.UsuarioDTO; // Importamos tu DTO de Usuario
import com.backApi.demo.Dto.ErrorResponseU; // Importamos tu DTO de Error
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // Para códigos de estado HTTP
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime; // Para el timestamp en ErrorResponse
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Para mapear listas a DTOs

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*") // Ajusta esto si necesitas seguridad de origen cruzado más estricta
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Método auxiliar para convertir Entidad Usuario a UsuarioDTO
    // Es crucial para evitar exponer datos sensibles como la contraseña
    private UsuarioDTO convertToDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUserId(usuario.getUserId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setPreferencias(usuario.getPreferencias());
        // No incluyas la contraseña (password) en el DTO
        // Si tu UsuarioDTO tiene 'rol' y tu entidad Usuario también, lo mapeas:
        // dto.setRol(usuario.getRol());
        return dto;
    }

    // CREAR USUARIO (Registro)
    // POST /api/usuarios
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) { // Recibe la entidad Usuario completa para el registro
        try {
            // **IMPORTANTE: Hashear la contraseña antes de guardarla**
            // Aquí deberías usar un PasswordEncoder (ej. BCryptPasswordEncoder)
            // usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Ejemplo con PasswordEncoder

            if (usuarioService.existeEmail(usuario.getEmail())) {
                ErrorResponseU errorResponse = new ErrorResponseU(
                        "El email ya está registrado.",
                        LocalDateTime.now().toString(),
                        HttpStatus.BAD_REQUEST.value()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            Usuario nuevoUsuario = usuarioService.guardarUsuario(usuario);
            // Devolvemos el DTO del nuevo usuario para no exponer la contraseña
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(nuevoUsuario)); // 201 Created
        } catch (Exception e) {
            ErrorResponseU errorResponse = new ErrorResponseU(
                    "Error al crear el usuario: " + e.getMessage(),
                    LocalDateTime.now().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // OBTENER TODOS LOS USUARIOS
    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<?> obtenerTodos() {
        try {
            List<UsuarioDTO> usuariosDto = usuarioService.getAllUsuariosDTO(); // Usamos el método que devuelve DTOs
            if (usuariosDto.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content si no hay usuarios
            }
            return ResponseEntity.ok(usuariosDto); // 200 OK con la lista de DTOs
        } catch (Exception e) {
            ErrorResponseU errorResponse = new ErrorResponseU(
                    "Error al obtener la lista de usuarios: " + e.getMessage(),
                    LocalDateTime.now().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // OBTENER UN USUARIO POR userId
    // GET /api/usuarios/{userId}
    @GetMapping("/{userId}") // Usamos userId como PathVariable
    public ResponseEntity<?> obtenerPorUserId(@PathVariable Integer userId) {
        try {
            Optional<UsuarioDTO> usuarioDto = usuarioService.getUsuarioDTOByUserId(userId); // Usamos el método que devuelve Optional<DTO>
            if (usuarioDto.isPresent()) {
                return ResponseEntity.ok(usuarioDto.get()); // 200 OK con el DTO
            } else {
                ErrorResponseU errorResponse = new ErrorResponseU(
                        "Usuario con ID " + userId + " no encontrado.",
                        LocalDateTime.now().toString(),
                        HttpStatus.NOT_FOUND.value()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404 Not Found
            }
        } catch (Exception e) {
            ErrorResponseU errorResponse = new ErrorResponseU(
                    "Error al obtener el usuario por ID: " + e.getMessage(),
                    LocalDateTime.now().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ACTUALIZAR UN USUARIO EXISTENTE
    // PUT /api/usuarios/{userId}
    @PutMapping("/{userId}") // Usamos userId como PathVariable
    public ResponseEntity<?> actualizarUsuario(@PathVariable Integer userId, @RequestBody UsuarioDTO usuarioActualizadoDto) { // Recibe DTO
        try {
            Optional<Usuario> existenteOptional = usuarioService.findByUserId(userId); // Buscar la entidad existente por userId
            if (existenteOptional.isEmpty()) {
                ErrorResponseU errorResponse = new ErrorResponseU(
                        "Usuario con ID " + userId + " no encontrado para actualizar.",
                        LocalDateTime.now().toString(),
                        HttpStatus.NOT_FOUND.value()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }

            Usuario usuario = existenteOptional.get();
            // Actualizar solo los campos que se permiten modificar desde el DTO
            usuario.setNombre(usuarioActualizadoDto.getNombre());
            usuario.setEmail(usuarioActualizadoDto.getEmail());
            usuario.setTelefono(usuarioActualizadoDto.getTelefono());
            usuario.setDireccion(usuarioActualizadoDto.getDireccion());
            usuario.setPreferencias(usuarioActualizadoDto.getPreferencias());
            // No actualizar la contraseña aquí; debería haber un endpoint separado para cambio de contraseña

            Usuario usuarioActualizado = usuarioService.actualizarUsuario(usuario); // Guardar los cambios
            return ResponseEntity.ok(convertToDto(usuarioActualizado)); // Devolvemos el DTO actualizado
        } catch (Exception e) {
            ErrorResponseU errorResponse = new ErrorResponseU(
                    "Error al actualizar el usuario: " + e.getMessage(),
                    LocalDateTime.now().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    // ELIMINAR UN USUARIO POR userId
    // DELETE /api/usuarios/{userId}
    @DeleteMapping("/{userId}") // Usamos userId como PathVariable
    public ResponseEntity<?> eliminarUsuario(@PathVariable Integer userId) {
        try {
            if (usuarioService.findByUserId(userId).isEmpty()) { // Verificar si existe antes de eliminar
                ErrorResponseU errorResponse = new ErrorResponseU(
                        "Usuario con ID " + userId + " no encontrado para eliminar.",
                        LocalDateTime.now().toString(),
                        HttpStatus.NOT_FOUND.value()
                );
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
            usuarioService.eliminarUsuario(userId); // Usar el método de servicio que elimina por userId
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content (eliminado exitosamente)
        } catch (Exception e) {
            ErrorResponseU errorResponse = new ErrorResponseU(
                    "Error al eliminar el usuario: " + e.getMessage(),
                    LocalDateTime.now().toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.value()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}