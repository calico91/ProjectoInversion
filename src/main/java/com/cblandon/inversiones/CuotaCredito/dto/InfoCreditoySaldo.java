package com.cblandon.inversiones.CuotaCredito.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InfoCreditoySaldo {


    private Integer id;
    private Double valorCuota;

    private LocalDate fechaCuota;
    private Integer numeroCuotas;
    private Integer cuotaNumero;


    private Double valorInteres;

    private Double valorCredito;

    private Double interesPorcentaje;

    private LocalDate fechaCredito;

    private Double interesHoy;

    private Double saldoCredito;
    private Double capitalPagado;
    private String ultimaCuotaPagada;


}
