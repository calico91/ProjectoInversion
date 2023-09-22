package com.cblandon.inversiones.Credito.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditoResponseDTO {

    private Integer id;
    private Double cantidadPrestada;
    private Integer cantidadCuotas;
    private Date fechaCredito;

}
