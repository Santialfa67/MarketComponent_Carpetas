// package com.backApi.demo.Service;
// Crea un nuevo archivo como PedidoService.java
package com.backApi.demo.Service;

import com.backApi.demo.Dto.PedidoRequest;
import com.backApi.demo.Model.Producto;
import com.backApi.demo.Model.Pedido; // Necesitarás crear una entidad Pedido
import com.backApi.demo.Repository.ProductoRepository;
import com.backApi.demo.Repository.PedidoRepository; // Necesitarás crear un PedidoRepository
import jakarta.transaction.Transactional; // Importante para la transacción
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository; // Asume que tienes un PedidoRepository

    @Transactional // Asegura que toda la operación sea atómica
    public String procesarPedido(PedidoRequest pedidoRequest) {
        // Validación básica
        if (pedidoRequest.getItems() == null || pedidoRequest.getItems().isEmpty()) {
            return "El pedido no contiene ítems.";
        }

        List<Producto> productosActualizados = new ArrayList<>();
        List<String> erroresStock = new ArrayList<>();

        for (PedidoRequest.ItemPedido item : pedidoRequest.getItems()) {
            Optional<Producto> optionalProducto = productoRepository.findById(item.getProductoId());

            if (optionalProducto.isPresent()) {
                Producto producto = optionalProducto.get();
                if (producto.getStock() != null && producto.getStock() >= item.getCantidad()) {
                    producto.setStock(producto.getStock() - item.getCantidad());
                    productosActualizados.add(producto);
                } else {
                    erroresStock.add("Stock insuficiente para: " + producto.getNombre() + ". Stock actual: " + producto.getStock() + ", pedido: " + item.getCantidad());
                }
            } else {
                erroresStock.add("Producto no encontrado con ID: " + item.getProductoId());
            }
        }

        if (!erroresStock.isEmpty()) {
            // Si hay errores de stock, revertir y devolver el mensaje
            return String.join("\n", erroresStock);
        }

        // Si todo está bien, guarda los productos con stock actualizado
        productoRepository.saveAll(productosActualizados);

        // Opcional: Crear un registro de pedido en la base de datos
        Pedido nuevoPedido = new Pedido(); // Necesitarás crear esta entidad y llenar sus campos
        nuevoPedido.setFechaPedido(LocalDateTime.now());
        // Aquí podrías guardar los items del pedido, el total, el usuario, etc.
        pedidoRepository.save(nuevoPedido);

        return "Pedido procesado exitosamente y stock actualizado.";
    }
}