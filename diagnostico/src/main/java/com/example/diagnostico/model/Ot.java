package com.example.diagnostico.model;

import lombok.Data;

import java.util.Date;

@Data
public class Ot {

    private Long id;
    private Long idCliente;
    private Long idEquipo;
    private Integer numeroOt;
    private String estado;
    private Date fechaIngreso;
    private Date fechaEntrega;
    private Double diagnosticoMonto;
    private Double repuestosMonto;
    private Double manoObraMonto;
    private Double totalCobrado;
    private String estadoPago;

}
