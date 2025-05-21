package com.backApi.demo.Controller;

import com.backApi.demo.Dto.ProductoDTO;
import com.backApi.demo.Model.Categoria;
import com.backApi.demo.Model.Producto;
import com.backApi.demo.Model.Proveedor;
import com.backApi.demo.Repository.CategoriaRepository;
import com.backApi.demo.Repository.ProductoRepository;
import com.backApi.demo.Repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    // Obtener todos los productos
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoRepository.findAll();
    }


    // Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Integer id) {
        return productoRepository.findById(id)
                .map(producto -> {
                    // Forzar la carga del proveedor (si la relación es lazy)
                    if (producto.getProveedor() != null) {
                        producto.getProveedor().getNombre(); // Esto fuerza la carga del proveedor
                    }
                    return ResponseEntity.ok(producto);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/categoria/{categoriaId}")
    public List<Producto> getProductosByCategoria(@PathVariable Integer categoriaId) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(categoriaId);
        if (categoriaOptional.isPresent()) {
            List<Producto> productos = productoRepository.findByCategoria(categoriaOptional.get());
            for (Producto producto : productos) {
                System.out.println("Producto: " + producto.getNombre() + ", Imagen URL: " + producto.getImagen()); // <--- Añade este log
            }
            return productos;
        } else {
            return List.of();
        }
    }

    // Crear nuevo producto usando DTO
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody ProductoDTO dto) {
        // Validar categoría
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(dto.getCategoriaId());
        if (categoriaOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Validar proveedor (si es requerido)
        Optional<Proveedor> proveedorOpt = Optional.empty();
        if (dto.getProveedorId() != null) {
            proveedorOpt = proveedorRepository.findById(dto.getProveedorId());
            if (proveedorOpt.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
        }

        Producto producto = new Producto();
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoria(categoriaOpt.get());
        producto.setImagen(dto.getImagen());
        producto.setStock(dto.getStock());
        proveedorOpt.ifPresent(producto::setProveedor); // Asigna proveedor si existe

        return ResponseEntity.ok(productoRepository.save(producto));
    }

    // Actualizar producto
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Integer id, @RequestBody ProductoDTO dto) {
        Optional<Producto> optionalProducto = productoRepository.findById(id);
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(dto.getCategoriaId());

        if (optionalProducto.isPresent() && categoriaOpt.isPresent()) {
            Producto producto = optionalProducto.get();
            producto.setNombre(dto.getNombre());
            producto.setDescripcion(dto.getDescripcion());
            producto.setPrecio(dto.getPrecio());
            producto.setCategoria(categoriaOpt.get());
            producto.setImagen(dto.getImagen());
            producto.setStock(dto.getStock());

            return ResponseEntity.ok(productoRepository.save(producto));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    // Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Integer id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}