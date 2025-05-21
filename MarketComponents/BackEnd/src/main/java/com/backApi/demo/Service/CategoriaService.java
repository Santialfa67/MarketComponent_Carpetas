package com.backApi.demo.Service;

import com.backApi.demo.Model.Categoria;
import com.backApi.demo.Repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Obtener todas las categorías
    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    // Obtener una categoría por ID
    public Optional<Categoria> getCategoriaById(Integer id) {
        return categoriaRepository.findById(id);
    }

    // Crear nueva categoría
    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    // Actualizar una categoría existente
    public Optional<Categoria> updateCategoria(Integer id, Categoria categoriaDetails) {
        return categoriaRepository.findById(id).map(categoria -> {
            categoria.setNombre(categoriaDetails.getNombre());
            categoria.setDescripcion(categoriaDetails.getDescripcion());
            return categoriaRepository.save(categoria);
        });
    }

    // Eliminar una categoría
    public boolean deleteCategoria(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
