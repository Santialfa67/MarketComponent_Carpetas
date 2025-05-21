package com.backApi.demo.Repository;

import com.backApi.demo.Model.Categoria;
import com.backApi.demo.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(Categoria categoria);

}
