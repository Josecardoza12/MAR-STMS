package com.example.bodega.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bodega")
public class Bodega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El ID de la OT es obligatorio")
    @Column(unique = true, nullable = false)
    private Long otId;

    private LocalDate fechaListo;

    private Integer diasEnBodega;

    private String estadoCobro;

    private Double montoBodegaje;
}
