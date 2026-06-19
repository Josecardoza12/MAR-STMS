package com.example.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Entity
@Table(name = "repuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Entidad que representa un repuesto en el inventario")
public class Repuesto extends RepresentationModel<Repuesto> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="repuestoId")
    @Schema(description = "Identificador único del repuesto", example = "1")
    private Long repuestoId;

    @Column(name="reparacionId")
    @Schema(description = "ID de la reparación asociada al uso del repuesto", example = "10")
    private Long reparacionId;

    @NotBlank(message = "El nombre del repuesto es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    @Schema(description = "Nombre del repuesto", example = "Carburador Honda GX160")
    private String nombre;

    @NotBlank(message = "El proveedor es obligatorio")
    @Size(max = 100, message = "El proveedor no puede superar 100 caracteres")
    @Schema(description = "Proveedor del repuesto", example = "Proveedor MAR Melipilla")
    private String proveedor;

    @NotNull(message = "El costo es obligatorio")
    @PositiveOrZero(message = "El costo no puede ser negativo")
    @Schema(description = "Costo del repuesto", example = "15000")
    private Double costo;

    @NotNull(message = "El precio sugerido es obligatorio")
    @PositiveOrZero(message = "El precio sugerido no puede ser negativo")
    @Column(name="precioSugerido")
    @Schema(description = "Precio sugerido de venta", example = "25000")
    private Double precioSugerido;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Schema(description = "Cantidad disponible en stock", example = "5")
    private Integer stock;
}
