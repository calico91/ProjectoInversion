package com.cblandon.inversiones.CuotaCredito.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

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
