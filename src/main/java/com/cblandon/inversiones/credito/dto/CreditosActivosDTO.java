package com.cblandon.inversiones.credito.dto;


import java.time.LocalDate;

public record CreditosActivosDTO(Integer idCliente, String nombres, String apellidos,
                                 String cedula, Integer idCredito, LocalDate fechaCredito, Double valorCredito) {
}
