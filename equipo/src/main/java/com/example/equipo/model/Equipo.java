package com.example.equipo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipo")
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "El id del cliente es obligatorio")
    @Column(nullable = false)
    private Long idCliente;

    @NotBlank(message = "El tipo de equipo es obligatorio")
    @Column(nullable = false )
    private String tipoEquipo;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false)
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false)
    private String modelo;

    @NotBlank(message = "El número de serie es obligatorio")
    @Column(nullable = false)
    private String numeroSerie;

    @NotBlank(message = "El estado de ingreso es obligatorio")
    @Column(nullable = false)
    private String estadoIngreso;




}
