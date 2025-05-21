package com.backApi.demo.Controller;

import com.backApi.demo.Model.DetallePedido;
import com.backApi.demo.Service.DetallePedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/detalles-pedidos") // Rutas en minúscula y consistentes
public class DetallePedidoController {

    private final DetallePedidoService detallePedidoService;

    @Autowired
    public DetallePedidoController(DetallePedidoService detallePedidoService) {
        this.detallePedidoService = detallePedidoService;
    }

    @GetMapping("/ver pedido")
    public ResponseEntity<List<DetallePedido>> getAllDetalles() {
        List<DetallePedido> detalles = detallePedidoService.getAllDetalles();
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DetallePedido> getDetalleById(@PathVariable Integer id) {
        return detallePedidoService.getDetalleById(id)
                .map(detalle -> new ResponseEntity<>(detalle, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<Optional<DetallePedido>> getDetallesByPedido(@PathVariable Integer pedidoId) {
        Optional<DetallePedido> detalles = detallePedidoService.getDetalleById(pedidoId);
        return new ResponseEntity<>(detalles, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<DetallePedido> createDetalle(@Valid @RequestBody DetallePedido detalle) {
        DetallePedido nuevoDetalle = detallePedidoService.saveDetalle(detalle);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DetallePedido> updateDetalle(
            @PathVariable Integer id,
            @Valid @RequestBody DetallePedido detalle) {

        if (!id.equals(detalle.getDetalleId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return detallePedidoService.getDetalleById(id)
                .map(existing -> {
                    DetallePedido actualizado = detallePedidoService.saveDetalle(detalle);
                    return new ResponseEntity<>(actualizado, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDetalle(@PathVariable Integer id) {
        if (detallePedidoService.existsById(id)) {
            detallePedidoService.deleteDetalle(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}