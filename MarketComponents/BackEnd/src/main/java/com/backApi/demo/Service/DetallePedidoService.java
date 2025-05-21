package com.backApi.demo.Service;


import com.backApi.demo.Model.DetallePedido;
import com.backApi.demo.Repository.DetallePedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> getAllDetalles() {
        return detallePedidoRepository.findAll();
    }

    public Optional<DetallePedido> getDetalleById(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    public DetallePedido saveDetalle(DetallePedido detalle) {
        return detallePedidoRepository.save(detalle);
    }

    public void deleteDetalle(Integer id) {
        detallePedidoRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return detallePedidoRepository.existsById(id);
    }
}
