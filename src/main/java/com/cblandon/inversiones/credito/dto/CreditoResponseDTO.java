package com.cblandon.inversiones.credito.dto;

import lombok.Builder;

import java.util.Date;


@Builder
public record CreditoResponseDTO(Integer id, Date fechaCredito) {
}
