package com.cblandon.inversiones.Excepciones;

import lombok.Data;

@Data
public class RequestException extends RuntimeException {
    private Integer status;

    public RequestException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
