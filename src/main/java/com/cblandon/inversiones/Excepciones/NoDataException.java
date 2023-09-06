package com.cblandon.inversiones.Excepciones;

public class NoDataException extends RuntimeException {

    private String code;

    public NoDataException(String message, String code) {
        super(message);
        this.code = code;
    }
}
