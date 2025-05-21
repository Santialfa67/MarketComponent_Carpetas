package com.backApi.demo.Model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoria_id")
    private Integer categoria_id;

    @Column(length = 255, nullable = false)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

}
