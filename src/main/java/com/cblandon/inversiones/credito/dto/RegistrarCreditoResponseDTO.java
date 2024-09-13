package com.cblandon.inversiones.credito.dto;

import lombok.Builder;

@Builder
public record RegistrarCreditoResponseDTO(String valorPrimerCuota, String cantidadCuotas, String valorCuotas,
                                          String fechaPago, String valorCredito, String nombreCliente) {
}


