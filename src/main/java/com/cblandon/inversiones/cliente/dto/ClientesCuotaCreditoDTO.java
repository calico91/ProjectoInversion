package com.cblandon.inversiones.cliente.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ClientesCuotaCreditoDTO(
        Integer idCliente, String nombres, String apellidos, String cedula, LocalDate fechaCredito, Double valorCredito,
        LocalDateTime fechaAbono, LocalDate fechaCuota, Double valorCuota, Double valorInteres, Integer idCredito) {
}
