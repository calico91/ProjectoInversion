package com.cblandon.inversiones.Credito.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrarCreditoRequestDTO {
    private Double cantidadPrestada;

    private Double interesPorcentaje;

    private Integer cantidadCuotas;

    private String cedulaTitularCredito;
    @JsonProperty("dia_pago")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate diaPago;
}