package com.cblandon.inversiones.credito.dto;


import com.cblandon.inversiones.excepciones.validation.anotation.ValidNullBlank;
import com.cblandon.inversiones.modalidad.Modalidad;
import com.cblandon.inversiones.utils.Constantes;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @ValidNullBlank
    private String cedulaTitularCredito;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCuota;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCredito;

    @NotNull(message = Constantes.NOT_NULL)
    private Double valorCredito;

    private Modalidad modalidad;

}