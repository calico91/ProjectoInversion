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

    private String valorPrimerCuota;
    private String cantidadCuotas;
    private String valorCuotas;
    private String fechaPago;
    private String valorCredito;
}


