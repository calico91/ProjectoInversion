package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.Getter;

@Getter
public class RequestException extends RuntimeException {
    private final MensajesErrorEnum mensajesErrorEnum;

    public RequestException(MensajesErrorEnum mensajesErrorEnum) {
        this.mensajesErrorEnum = mensajesErrorEnum;
    }
}




