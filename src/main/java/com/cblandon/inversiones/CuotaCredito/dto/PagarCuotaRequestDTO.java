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

    /// para pago de cuota normal
    private Double valorAbonado;

    /// para pago de solo interes
    private Double interesAbonado;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaAbono;
}
