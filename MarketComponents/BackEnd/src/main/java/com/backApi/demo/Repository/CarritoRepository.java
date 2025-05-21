package com.backApi.demo.Repository;

import com.backApi.demo.Model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoRepository extends JpaRepository<Carrito, Integer> {
}
