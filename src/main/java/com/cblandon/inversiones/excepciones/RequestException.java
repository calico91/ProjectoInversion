package com.cblandon.inversiones.excepciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestException extends RuntimeException {
    private final Integer status;

    public RequestException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
