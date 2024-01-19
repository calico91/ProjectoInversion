package com.cblandon.inversiones.cliente.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfoClientesCuotaCreditoDTO {

    private Integer idCliente;
    private String nombres;
    private String apellidos;
    private String cedula;
    private String fechaCredito;
    private Double valorCredito;
    private String fechaAbono;
    private String fechaCuota;
    private Double valorCuota;
    private Double valorInteres;
    private Integer idCredito;

}
