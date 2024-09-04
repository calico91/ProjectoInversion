package com.cblandon.inversiones.credito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaldarCreditoDTO(
        @JsonProperty("id_credito")
        Integer idCredito,
        @JsonProperty("valor_pagado")
        Double valorPagado
) {
}
