package com.cblandon.inversiones.excepciones.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {
    private Integer status;
    private String message;
}
