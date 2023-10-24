package com.cblandon.inversiones.Credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarCreditoResponseDTO {

    private Double valorPrimerCuota;
    private Integer cantidadCuotas;
    private Double valorCuotas;
    private String fechaPago;

}


