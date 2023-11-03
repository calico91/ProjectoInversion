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
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaAbono;
    private String tipoAbono;


}
