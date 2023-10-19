package com.cblandon.inversiones.Credito.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class RegistrarCreditoRequestDTO {
    private Double cantidadPrestada;

    private Double interesPorcentaje;

    private Integer cantidadCuotas;

    private String cedulaTitularCredito;

    @JsonProperty("fecha_cuota")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCuota;

    @JsonProperty("fecha_credito")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate fechaCredito;
}