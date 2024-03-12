package com.cblandon.inversiones.cuotacredito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record AbonoPorIdDTO(Integer id, Double valorAbonado, LocalDateTime fechaAbono, String tipoAbono,
                            @JsonProperty("cantidadCuotas")
                            Integer numeroCuotas,
                            @JsonProperty("cuotasPagadas")
                            Integer cuotaNumero) {
}
