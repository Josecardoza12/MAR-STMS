package com.example.orden_trabajo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orden_trabajo")
public class OrdenTrabajo extends RepresentationModel<OrdenTrabajo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Identificador único de la ot",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotNull(message = "El id del cliente es obligatorio")
    @Schema(
            description = "Id del cliente",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idCliente;

    @NotNull(message = "El id del equipo es obligatorio")
    @Schema(
            description = "Id del equipo",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idEquipo;

    @NotNull(message = "El numero de ot es obligatorio")
    @Column(nullable = false , unique = true)
    @Schema(
            description = "Número único utilizado para identificar la orden de trabajo.",
            example = "1025",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Integer numeroOt;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Schema(
            description = "Estado actual de la orden de trabajo dentro del proceso de reparación.",
            example = "En reparación",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estado;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @Column(nullable = false)
    @Schema(
            description = "Fecha en la que el equipo fue ingresado al servicio técnico.",
            example = "2026-06-04",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Date fechaIngreso;

    @Schema(
            description = "Fecha estimada o registrada de entrega del equipo reparado.",
            example = "2026-06-10"
    )
    private Date fechaEntrega;

    @Schema(
            description = "Costo asociado al diagnóstico técnico realizado al equipo.",
            example = "15000.0"
    )
    private Double diagnosticoMonto;

    @Schema(
            description = "Monto total correspondiente a los repuestos utilizados en la reparación.",
            example = "45990.0"
    )
    private Double repuestosMonto;

    @Schema(
            description = "Costo de la mano de obra aplicada durante el proceso de reparación.",
            example = "30000.0"
    )
    private Double manoObraMonto;

    @Schema(
            description = "Monto total cobrado al cliente por el servicio técnico realizado.",
            example = "90990.0"
    )
    private Double totalCobrado;

    @NotBlank(message = "El estado de pago es obligatorio")
    @Column(nullable = false)
    @Schema(
            description = "Estado actual del pago asociado a la orden de trabajo.",
            example = "Pendiente",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estadoPago;


}
