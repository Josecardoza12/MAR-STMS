package com.example.equipo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "equipo")
public class Equipo extends RepresentationModel<Equipo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(
            description = "Identificador único del equipo",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;


    @NotNull(message = "El id del cliente es obligatorio")
    @Column(nullable = false)
    @Schema(
            description = "Id del cliente",
            example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private Long idCliente;

    @NotBlank(message = "El tipo de equipo es obligatorio")
    @Column(nullable = false )
    @Schema(
            description = "Tipo de maquinaria o herramienta ingresada al servicio técnico.",
            example = "Generador eléctrico",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String tipoEquipo;

    @NotBlank(message = "La marca es obligatoria")
    @Column(nullable = false)
    @Schema(
            description = "Fabricante del equipo ingresado a reparación.",
            example = "Stihl",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Column(nullable = false)
    @Size(min = 5 )
    @Size(max = 20)
    @Schema(
            description = "Modelo del equipo registrado para diagnóstico y reparación.",
            example = "FS 250",
            minLength = 5,
            maxLength = 20,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String modelo;

    @NotBlank(message = "El número de serie es obligatorio")
    @Column(nullable = false)
    @Schema(
            description = "Código o número de serie único del equipo.",
            example = "STHL-45872-2026",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String numeroSerie;

    @NotBlank(message = "El estado de ingreso es obligatorio")
    @Column(nullable = false)
    @Schema(
            description = "Condición o estado en el que el equipo ingresa al taller.",
            example = "No enciende",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String estadoIngreso;




}
