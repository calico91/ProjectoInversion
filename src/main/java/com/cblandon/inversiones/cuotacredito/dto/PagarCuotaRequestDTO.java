package com.cblandon.inversiones.cuotacredito.dto;

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
    private String tipoAbono;
    private String estadoCredito;
    private boolean abonoExtra;


}
