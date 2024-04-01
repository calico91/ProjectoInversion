package com.cblandon.inversiones.excepciones.request_exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestException extends RuntimeException {
    private final RequestExceptionMensajes requestExceptionMensajes;

    public RequestException(RequestExceptionMensajes requestExceptionMensajes) {
        this.requestExceptionMensajes = requestExceptionMensajes;
    }
}




