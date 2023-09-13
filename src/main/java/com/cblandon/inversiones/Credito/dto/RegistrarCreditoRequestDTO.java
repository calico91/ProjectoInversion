package com.cblandon.inversiones.Credito.dto;


import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrarCreditoRequestDTO {
    private Double cantidadPrestada;

    private Double interesPorcentaje;

    private Integer cantidadCuotas;

    private String cedulaTitularCredito;

    private Date diaPago;
}
