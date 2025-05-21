package com.backApi.demo.Controller;

import com.backApi.demo.Dto.PedidoRequest;
import com.backApi.demo.Service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping("/procesar")
    public ResponseEntity<Void> procesarPedido(@RequestBody PedidoRequest pedidoRequest) {
        String resultado = pedidoService.procesarPedido(pedidoRequest);
        if (resultado.startsWith("Pedido procesado exitosamente")) {
            return new ResponseEntity<>(null, HttpStatus.OK); // <--- Intenta esto

        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}