// backend/com/backApi/demo/Service/UsuarioService.java
package com.backApi.demo.Service;

import com.backApi.demo.Model.Usuario;
import com.backApi.demo.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Método para guardar un usuario (usado en el registro)
    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        return usuarioRepository.save(usuario);
    }

    // Método para verificar si un email ya existe (usado en el registro)
    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    // Método para buscar un usuario por email (¡Nuevo o Asegúrate de que exista!)
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Método para verificar credenciales (si aún lo usas en el AuthController, si no, puedes quitarlo)
    // Si lo mantienes, podrías modificarlo para devolver Optional<Usuario> en lugar de boolean
    public boolean verificarCredenciales(String email, String password) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            // ¡IMPORTANTE! Esto es para pruebas. En producción, compara contraseñas hasheadas.
            return usuario.getPassword().equals(password);
        }
        return false;
    }

    // Otros métodos existentes
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }
}