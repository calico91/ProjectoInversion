package com.cblandon.inversiones.CuotaCredito.dto;

import com.cblandon.inversiones.Credito.Credito;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CuotasCreditoDTO {


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

    private String tipoAbono;

    private LocalDate fechaCredito;


}
