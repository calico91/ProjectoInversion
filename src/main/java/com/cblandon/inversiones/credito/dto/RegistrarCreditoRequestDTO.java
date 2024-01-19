package com.cblandon.inversiones.credito.dto;


import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrarCreditoRequestDTO {
    private Double interesPorcentaje;

    private Integer cantidadCuotas;

    private String cedulaTitularCredito;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCuota;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCredito;

    private Double valorCredito;

    private String modalidad;

}