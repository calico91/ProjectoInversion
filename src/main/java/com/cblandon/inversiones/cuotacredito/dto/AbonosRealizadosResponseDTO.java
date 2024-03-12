package com.cblandon.inversiones.cuotacredito.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AbonosRealizadosResponseDTO(Double valorAbonado, String tipoAbono, LocalDate fechaAbono,
                                          Integer cuotaNumero, String nombres, String apellidos) {
}
