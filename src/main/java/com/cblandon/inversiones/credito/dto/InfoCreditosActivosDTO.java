package com.cblandon.inversiones.credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoCreditosActivosDTO {

    private Integer idCliente;
    private Integer idCredito;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String fechaCredito;
    private Double valorCredito;
}
