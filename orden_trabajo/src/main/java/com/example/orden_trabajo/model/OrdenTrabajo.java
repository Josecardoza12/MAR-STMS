package com.example.orden_trabajo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orden_trabajo")
public class OrdenTrabajo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El id del cliente es obligatorio")
    private Long idCliente;

    @NotNull(message = "El id del equipo es obligatorio")
    private Long idEquipo;

    @NotNull(message = "El numero de ot es obligatorio")
    @Column(nullable = false , unique = true)
    private Integer numeroOt;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(nullable = false)
    private Date fechaIngreso;

    private Date fechaEntrega;

    private Double diagnosticoMonto;

    private Double repuestosMonto;

    private Double manoObraMonto;

    private Double totalCobrado;

    @NotBlank(message = "El estado de pago es obligatorio")
    @Column(nullable = false)
    private String estadoPago;


}
