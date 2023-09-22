package com.cblandon.inversiones.Credito.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditoAllResponseDTO {


    private Integer id;
    private Double cantidadPrestada;

    private Double interesPorcentaje;

    private Integer cantidadCuotas;

    private Double saldo;

    private String usuarioCreador;

    private String estadoCredito;

    private Date fechaCredito;
}
