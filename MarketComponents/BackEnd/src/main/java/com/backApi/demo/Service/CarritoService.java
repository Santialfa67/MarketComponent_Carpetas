package com.backApi.demo.Service;

import com.backApi.demo.Model.Carrito;
import com.backApi.demo.Repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service

public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    public List<Carrito> getAllCarritoItems() {
        return carritoRepository.findAll();
    }

    public Optional<Carrito> getCarritoItemById(Integer id) {
        return carritoRepository.findById(id);
    }

    public Carrito addCarritoItem(Carrito carrito) {
        carrito.setFechaAgregado(LocalDateTime.now());
        return carritoRepository.save(carrito);
    }

    public Optional<Carrito> updateCarritoItem(Integer id, Carrito carritoDetails) {
        return carritoRepository.findById(id).map(carrito -> {
            carrito.setUsuario(carritoDetails.getUsuario());
            carrito.setProducto(carritoDetails.getProducto());
            carrito.setCantidad(carritoDetails.getCantidad());
            carrito.setFechaAgregado(LocalDateTime.now());
            return carritoRepository.save(carrito);
        });
    }

    public boolean deleteCarritoItem(Integer id) {
        if (carritoRepository.existsById(id)) {
            carritoRepository.deleteById(id);
            return true;
        }
        return false;
    }
}