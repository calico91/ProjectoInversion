package com.cblandon.inversiones.cliente.dto;

import lombok.Builder;

@Builder
public record ClientesCuotaCreditoDTO(
        Integer idCliente, String nombres, String apellidos, String cedula, String fechaCredito, Double valorCredito,
        String fechaAbono, String fechaCuota, Double valorCuota, Double valorInteres, Integer idCredito) {
}
