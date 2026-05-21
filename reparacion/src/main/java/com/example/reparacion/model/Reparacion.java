package com.example.reparacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "reparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reparacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reparacion_id")
    private Long reparacionId;

    @NotNull(message = "El ID de la OT es obligatorio")
    @Column(name = "ot_id", nullable = false)
    private Long otId;



    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino")
    private LocalDate fechaTermino;

    @NotBlank(message = "El detalle del trabajo no puede estar vacío")
    @Column(name = "detalle_trabajo", nullable = false)
    private String detalleTrabajo;

    @Column(name = "estado", nullable = false)
    private String estado;


}
