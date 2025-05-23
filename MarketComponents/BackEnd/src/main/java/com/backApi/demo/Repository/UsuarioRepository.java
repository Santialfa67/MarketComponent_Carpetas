// backend/com/backApi/demo/Repository/UsuarioRepository.java
package com.backApi.demo.Repository;

import com.backApi.demo.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    // Añade este método:
    Optional<Usuario> findByEmail(String email);
}