package com.cblandon.inversiones.Excepciones;

import lombok.Data;

@Data
public class NoDataException extends RuntimeException {

    private Integer status;

    public NoDataException(String message, Integer status) {
        super(message);
        this.status = status;
    }
}
