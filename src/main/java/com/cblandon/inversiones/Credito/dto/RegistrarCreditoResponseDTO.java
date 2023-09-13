package com.cblandon.inversiones.Credito.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrarCreditoResponseDTO {

    private Double cantidadPrestada;

    private Integer cantidadCuotas;

    private String cedulaTitularCredito;

    private Date diaPago;

    private Double valorCuota;

}


