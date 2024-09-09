package com.cblandon.inversiones.credito.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SaldarCreditoResponseDTO(
        @JsonProperty("id_credito")
        Integer idCredito,
        @JsonProperty("valor_pagado")
        Double valorPagado,
        @JsonProperty("nombre_cliente")
        String nombreCliente,
        @JsonProperty("valor_credito")
        Double valorCredito
) {

}
