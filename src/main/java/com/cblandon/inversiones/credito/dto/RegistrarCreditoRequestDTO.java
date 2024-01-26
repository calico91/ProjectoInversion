package com.cblandon.inversiones.credito.dto;


import com.cblandon.inversiones.utils.Constantes;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrarCreditoRequestDTO {

    @NotNull(message = Constantes.NOT_NULL)
    private Double interesPorcentaje;

    @NotNull(message = Constantes.NOT_NULL)
    private Integer cantidadCuotas;

    @NotNull(message = Constantes.NOT_NULL)
    @NotBlank(message = Constantes.NOT_BLANK)
    private String cedulaTitularCredito;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCuota;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCredito;

    @NotNull(message = Constantes.NOT_NULL)
    private Double valorCredito;

    @NotNull(message = Constantes.NOT_NULL)
    @NotBlank(message = Constantes.NOT_BLANK)
    private String modalidad;

}