package com.cblandon.inversiones.cuotacredito.dto;

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
public class PagarCuotaRequestDTO {


    private Double valorAbonado;

    private Double valorInteres;

    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaAbono;

    @NotBlank(message = Constantes.NOT_BLANK)
    private String tipoAbono;

    @NotBlank(message = Constantes.NOT_BLANK)
    private String estadoCredito;

    private boolean abonoExtra;


}
