package com.example.finanzas.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Pago {

    private Long id;


    private Long otId;


    private LocalDate fecha;

    private Double monto;


    private String formaPago;

    private String estado;
}
