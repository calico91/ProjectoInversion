package com.cblandon.inversiones.cuotacredito.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AbonosRealizadosResponseDTO {

    private Double valorAbonado;
    private String tipoAbono;
    private LocalDate fechaAbono;
    private Integer cuotaNumero;
    private String nombres;
    private String apellidos;

}
