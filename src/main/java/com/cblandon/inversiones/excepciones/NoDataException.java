package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
public class NoDataException extends RuntimeException {

    private final MensajesErrorEnum mensajesErrorEnum;

    public NoDataException(MensajesErrorEnum mensajesErrorEnum) {
        this.mensajesErrorEnum = mensajesErrorEnum;
    }
}
