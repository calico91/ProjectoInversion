package com.cblandon.inversiones.credito.dto;


import com.cblandon.inversiones.excepciones.validation.anotation.ValidNullBlank;
import com.cblandon.inversiones.modalidad.Modalidad;
import com.cblandon.inversiones.utils.Constantes;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;


@Builder
public record RegistrarCreditoRequestDTO(@NotNull(message = Constantes.NOT_NULL)
                                         Double interesPorcentaje,

                                         @NotNull(message = Constantes.NOT_NULL)
                                         Integer cantidadCuotas,

                                         @ValidNullBlank
                                         String cedulaTitularCredito,

                                         //@JsonFormat(pattern = "dd/MM/yyyy")
                                         LocalDate fechaCuota,

                                         //@JsonFormat(pattern = "dd/MM/yyyy")
                                         LocalDate fechaCredito,

                                         @NotNull(message = Constantes.NOT_NULL)
                                         Double valorCredito,
                                         Modalidad modalidad) {


}