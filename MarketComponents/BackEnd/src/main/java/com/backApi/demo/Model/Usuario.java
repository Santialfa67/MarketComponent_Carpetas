package com.backApi.demo.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(length = 255, nullable = false)
    private String nombre;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

//    @Column(length = 255, nullable = false)
//    private String contraseña;

    // ¡CAMBIO AQUÍ! Mapea la propiedad 'password' a la columna 'contraseña' en la DB
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 10)
    private String telefono;

    @CreationTimestamp
    @Column(name = "fecha_registro", updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(columnDefinition = "TEXT")
    private String direccion;

    @Column(columnDefinition = "TEXT")
    private String preferencias;

}