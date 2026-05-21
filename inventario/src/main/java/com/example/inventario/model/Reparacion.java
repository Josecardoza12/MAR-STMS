package com.example.inventario.model;

import lombok.Data;

import java.time.LocalDate;

@Data

public class Reparacion {

    private Long reparacionId;
    private Long otId;

    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private String detalleTrabajo;
    private String estado;

}
