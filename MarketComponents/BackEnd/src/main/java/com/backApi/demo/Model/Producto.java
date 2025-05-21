package com.backApi.demo.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "producto_id")
//    @JsonBackReference
    private Integer productoId;

    @Column(nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal precio;

//    @ManyToOne
//    @JoinColumn(name = "categoria_id", nullable = false)
//    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER) // Asegúrate de que siga siendo EAGER
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @Column(length = 1000)
    private String imagen;

    @Column(nullable = false)
    private Integer stock;

//    @ManyToOne
//    @JoinColumn(name = "proveedor_id")
//    //@JsonBackReference
//    private Proveedor proveedor;

    @ManyToOne(fetch = FetchType.EAGER) // Asegúrate de que siga siendo EAGER
    @JoinColumn(name = "proveedor_id")
    @JsonManagedReference // <--- ¡CAMBIA A ESTA ANOTACIÓN!
    private Proveedor proveedor;

}