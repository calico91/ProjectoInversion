package com.cblandon.inversiones.cuota_credito.dto;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
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

    private String tipoAbono;

    private LocalDate fechaAbono;

    private Double interesMora;

    private Integer diasMora;

    private String modalidad;

}


