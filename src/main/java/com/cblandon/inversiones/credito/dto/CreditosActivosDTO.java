package com.cblandon.inversiones.credito.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record CreditosActivosDTO(Integer idCliente, String nombres, String apellidos,
                                 String cedula,Integer idCredito, LocalDate fechaCredito, Double valorCredito) {
}
