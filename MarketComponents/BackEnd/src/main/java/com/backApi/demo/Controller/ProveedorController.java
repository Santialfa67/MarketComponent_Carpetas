package com.backApi.demo.Controller;

import com.backApi.demo.Model.Proveedor;
import com.backApi.demo.Service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/proveedores")
@CrossOrigin(origins = "*")

public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    // Obtener todos los proveedores
    @GetMapping
    public List<Proveedor> getAllProveedores() {
        return proveedorService.getAllProveedores();
    }

    // Obtener proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> getProveedorById(@PathVariable Integer id) {
        Optional<Proveedor> proveedor = proveedorService.getProveedorById(id);
        return proveedor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear nuevo proveedor
    @PostMapping
    public Proveedor createProveedor(@RequestBody Proveedor proveedor) {
        return proveedorService.createProveedor(proveedor);
    }

    // Actualizar proveedor existente
    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> updateProveedor(@PathVariable Integer id, @RequestBody Proveedor proveedorDetails) {
        Optional<Proveedor> updated = proveedorService.updateProveedor(id, proveedorDetails);
        return updated.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Eliminar proveedor
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProveedor(@PathVariable Integer id) {
        if (proveedorService.deleteProveedor(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}