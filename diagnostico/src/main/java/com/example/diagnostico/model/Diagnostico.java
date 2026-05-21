package com.example.diagnostico.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@Entity
@Table(name = "diagnostico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diagnostico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diagnosticoId;

    @NotNull
    @Positive
    @Column(name = "orden_trabajo_ot_id", nullable = false)
    private Long otId;

    @NotBlank
    @Size(max = 255)
    private String descripcion;

    @NotBlank
    @Size(max = 255)
    private String causaProbable;

    @NotBlank
    @Size(max = 20)
    private String hallazgo;

    @NotBlank
    @Pattern(regexp = "S|N")
    private String estado;

    @PastOrPresent
    private LocalDate fechaDiagnostico;
}
