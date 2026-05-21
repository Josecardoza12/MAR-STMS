package com.example.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "repuesto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="repuestoId")
    private Long repuestoId;


    @Column(name="reparacionId")
    private Long reparacionId;
    @NotBlank(message = "El nombre del repuesto es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombre;

    @NotBlank(message = "El proveedor es obligatorio")
    @Size(max = 100, message = "El proveedor no puede superar 100 caracteres")
    private String proveedor;

    @NotNull(message = "El costo es obligatorio")
    @PositiveOrZero(message = "El costo no puede ser negativo")
    private Double costo;

    @NotNull(message = "El precio sugerido es obligatorio")
    @PositiveOrZero(message = "El precio sugerido no puede ser negativo")
    @Column(name="precioSugerido")
    private Double precioSugerido;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}

