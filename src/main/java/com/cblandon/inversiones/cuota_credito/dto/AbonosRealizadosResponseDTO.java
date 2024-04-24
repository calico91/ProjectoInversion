package com.cblandon.inversiones.cuota_credito.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record AbonosRealizadosResponseDTO(Integer id, Double valorAbonado, String tipoAbono, LocalDate fechaAbono,
                                          Integer cuotaNumero, String nombres, String apellidos) {
}
