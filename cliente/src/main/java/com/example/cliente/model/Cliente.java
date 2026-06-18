package com.example.cliente.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cliente")
public class Cliente extends RepresentationModel<Cliente> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Schema(
            description = "Identificador único del cliente",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false , name = "nombre")
    @Schema(
            description = "Nombre del cliente",
            example = "Jose Cardoza",
            maxLength = 100,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String nombre;

    @NotBlank(message = "El rut es obligatorio")
    @Column(unique = true , nullable = false , name = "rut")
    @Schema(
            description = "RUT del cliente",
            example = "12345678-9",
            maxLength = 12,
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String rut;


    @Schema(
            description = "Telefono del cliente",
            example = "+569 71269419",
            maxLength = 15
    )
    @Column(name = "telefono")
    private String telefono;


    @Schema(
            description = "Direccion del cliente",
            example = "Lagos 2 #puyehue 621, melipilla",
            maxLength = 100
    )
    @Column(name = "direccion")
    private String direccion;


    @Schema(
            description = "Tipo de cliente",
            example = "Frecuente",
            maxLength = 30
    )
    @Column(name = "tipo_cliente")
    private String tipo_Cliente;
}
