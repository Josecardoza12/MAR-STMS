package com.example.reparacion.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Entity
@Table(name = "reparacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entidad que representa una reparación asociada a una orden de trabajo")
public class Reparacion extends RepresentationModel<Reparacion> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reparacion_id")
    @Schema(description = "Identificador único de la reparación", example = "1")
    private Long reparacionId;

    @NotNull(message = "El ID de la OT es obligatorio")
    @Column(name = "ot_id", nullable = false)
    @Schema(description = "ID de la orden de trabajo asociada", example = "10")
    private Long otId;

    @Column(name = "fecha_inicio")
    @Schema(description = "Fecha de inicio de la reparación", example = "2024-05-10")
    private LocalDate fechaInicio;

    @Column(name = "fecha_termino")
    @Schema(description = "Fecha de término de la reparación", example = "2024-05-12")
    private LocalDate fechaTermino;

    @NotBlank(message = "El detalle del trabajo no puede estar vacío")
    @Column(name = "detalle_trabajo", nullable = false)
    @Schema(description = "Detalle del trabajo realizado", example = "Cambio de carburador y ajuste de motor")
    private String detalleTrabajo;

    @Column(name = "estado", nullable = false)
    @Schema(description = "Estado de la reparación", example = "EN_REPARACION")
    private String estado;
}
