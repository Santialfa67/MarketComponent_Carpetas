package com.backApi.demo.Controller;

import com.backApi.demo.Model.Carrito;
import com.backApi.demo.Service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/carrito")

public class CarritoController {
    @Autowired
    private CarritoService carritoService;

    // Obtener todos los productos del carrito
    @GetMapping
    public List<Carrito> getAllItems() {
        return carritoService.getAllCarritoItems();
    }

    // Obtener un ítem del carrito por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Carrito> getItemById(@PathVariable Integer id) {
        Optional<Carrito> item = carritoService.getCarritoItemById(id);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Agregar un producto al carrito
    @PostMapping
    public Carrito addItemToCarrito(@RequestBody Carrito carrito) {
        return carritoService.addCarritoItem(carrito);
    }

    // Actualizar un ítem del carrito
    @PutMapping("/{id}")
    public ResponseEntity<Carrito> updateItem(@PathVariable Integer id, @RequestBody Carrito carritoDetails) {
        Optional<Carrito> updated = carritoService.updateCarritoItem(id, carritoDetails);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar un ítem del carrito
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Integer id) {
        if (carritoService.deleteCarritoItem(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }
}