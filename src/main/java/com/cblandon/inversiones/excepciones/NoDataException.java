package com.cblandon.inversiones.excepciones;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoDataException extends RuntimeException {

    private final Integer status;

    public NoDataException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
