package com.example.diagnostico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Slf4j
@Entity
@Table(name = "diagnostico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un diagnóstico técnico realizado a una orden de trabajo")
public class Diagnostico extends RepresentationModel<Diagnostico> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del diagnóstico", example = "1")
    private Long diagnosticoId;

    @NotNull
    @Positive
    @Column(name = "orden_trabajo_ot_id", nullable = false)
    @Schema(description = "ID de la orden de trabajo asociada", example = "10")
    private Long otId;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Descripción del diagnóstico", example = "Equipo no enciende")
    private String descripcion;

    @NotBlank
    @Size(max = 255)
    @Schema(description = "Causa probable del problema", example = "Falla en la placa de encendido")
    private String causaProbable;

    @NotBlank
    @Size(max = 20)
    @Schema(description = "Hallazgo principal", example = "CORTO")
    private String hallazgo;

    @NotBlank
    @Pattern(regexp = "S|N")
    @Schema(description = "Estado del diagnóstico (S = activo, N = inactivo)", example = "S")
    private String estado;

    @PastOrPresent
    @Schema(description = "Fecha en que se realizó el diagnóstico", example = "2024-05-10")
    private LocalDate fechaDiagnostico;
}
