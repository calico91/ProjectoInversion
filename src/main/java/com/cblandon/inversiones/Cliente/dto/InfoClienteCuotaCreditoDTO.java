package com.cblandon.inversiones.Cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoClienteCuotaCreditoDTO {

    private String nombres;
    private String apellidos;
    private String cedula;
    private String fechaCredito;
    private Integer idCuotaCredito;
    private Integer cuotaNumero;
    private String fechaAbono;
    private String fechaCuota;
    private Integer numeroCuotas;
    private Double valorAbonado;
    private Double coutaCapital;
    private Double valorCredito;
    private Double valorCuota;
    private Double valorInteres;
    private Integer idCredito;
}
