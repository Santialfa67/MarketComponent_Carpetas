package com.backApi.demo.Controller;

import com.backApi.demo.Model.Usuario;
import com.backApi.demo.Service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.existeEmail(usuario.getEmail())) {
            return ResponseEntity.badRequest().build(); // Email ya registrado
        }
        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }

    // Obtener todos los usuarios
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    // Obtener un usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.obtenerPorId(id);
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un usuario existente
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuarioActualizado) {
        Optional<Usuario> existente = usuarioService.obtenerPorId(id);
        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = existente.get();
        usuario.setNombre(usuarioActualizado.getNombre());
        usuario.setEmail(usuarioActualizado.getEmail());
        usuario.setContraseña(usuarioActualizado.getContraseña());
        usuario.setTelefono(usuarioActualizado.getTelefono());
        usuario.setFechaRegistro(usuarioActualizado.getFechaRegistro());
        usuario.setDireccion(usuarioActualizado.getDireccion());
        usuario.setPreferencias(usuarioActualizado.getPreferencias());

        return ResponseEntity.ok(usuarioService.guardarUsuario(usuario));
    }

    // Eliminar un usuario por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        if (usuarioService.obtenerPorId(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
    }