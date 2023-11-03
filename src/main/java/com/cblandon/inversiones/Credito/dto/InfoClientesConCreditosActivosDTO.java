package com.cblandon.inversiones.Credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoClientesConCreditosActivosDTO {

    private Integer idCliente;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String fechaCredito;
    private Double valorCredito;
}
