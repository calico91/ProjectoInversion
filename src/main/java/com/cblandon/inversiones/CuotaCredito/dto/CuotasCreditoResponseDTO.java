package com.cblandon.inversiones.CuotaCredito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuotasCreditoResponseDTO {

    private Integer id;
    private Double valorCuota;

    private LocalDate fechaCuota;

    private Integer numeroCuotas;

    private Integer cuotaNumero;

    private Double valorAbonado;

    private Double valorCapital;

    private Double valorInteres;

    private Double valorCredito;

    private Double interesPorcentaje;

    private LocalDate fechaAbono;
}


