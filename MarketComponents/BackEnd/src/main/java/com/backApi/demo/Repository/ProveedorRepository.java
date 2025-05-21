package com.backApi.demo.Repository;

import com.backApi.demo.Model.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProveedorRepository extends JpaRepository<Proveedor,Integer> {
}
