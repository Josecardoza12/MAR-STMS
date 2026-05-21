package com.example.equipo.model;


import lombok.Data;

@Data
public class Cliente {
    private Long id;


    private String nombre;


    private String rut;

    private String telefono;

    private String direccion;

    private String tipo_Cliente;
}
