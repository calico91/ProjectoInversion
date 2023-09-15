package com.cblandon.inversiones.Credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditoResponseDTO {

    private Double cantidadPrestada;
    private Double valorCuota;
    private Integer cantidadCuotas;

}
