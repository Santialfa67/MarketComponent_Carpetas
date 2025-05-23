package com.backApi.demo.Service;

import com.backApi.demo.Model.Usuario;
import com.backApi.demo.Repository.UsuarioRepository;
import com.backApi.demo.Dto.UsuarioDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }


    public Usuario guardarUsuario(Usuario usuario) {
        if (usuario.getFechaRegistro() == null) {
            usuario.setFechaRegistro(LocalDateTime.now());
        }
        return usuarioRepository.save(usuario);
    }


    public boolean existeEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }


    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }


    public Usuario autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(email);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();


            if (usuario.getPassword().equals(password)) {
                return usuario;
            }
        }
        return null;
    }

    public List<UsuarioDTO> getAllUsuariosDTO() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    public Optional<UsuarioDTO> getUsuarioDTOByUserId(Integer userId) { // Nombre modificado
        return usuarioRepository.findByUserId(userId)
                .map(this::convertToDto);
    }


    private UsuarioDTO convertToDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUserId(usuario.getUserId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setTelefono(usuario.getTelefono());
        dto.setDireccion(usuario.getDireccion());
        dto.setPreferencias(usuario.getPreferencias());

        return dto;
    }


    public Optional<Usuario> findByUserId(Integer userId) {
        return usuarioRepository.findByUserId(userId);
    }

    public Usuario actualizarUsuario(Usuario usuario) {

        return usuarioRepository.save(usuario);
    }


    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void eliminarUsuario(Integer userId) {
        usuarioRepository.deleteByUserId(userId);
    }
}