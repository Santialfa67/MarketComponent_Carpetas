package com.backApi.demo.Service;

import com.backApi.demo.Model.Proveedor;
import com.backApi.demo.Repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> getAllProveedores() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> getProveedorById(Integer id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor createProveedor(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Optional<Proveedor> updateProveedor(Integer id, Proveedor proveedorDetails) {
        return proveedorRepository.findById(id).map(proveedor -> {
            proveedor.setNombre(proveedorDetails.getNombre());
            proveedor.setContacto(proveedorDetails.getContacto());
            proveedor.setDireccion(proveedorDetails.getDireccion());
            proveedor.setProductosOfrecidos(proveedorDetails.getProductosOfrecidos());
            return proveedorRepository.save(proveedor);
        });
    }

    public boolean deleteProveedor(Integer id) {
        if (proveedorRepository.existsById(id)) {
            proveedorRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
