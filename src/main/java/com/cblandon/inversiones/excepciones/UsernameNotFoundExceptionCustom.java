package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.Getter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public class UsernameNotFoundExceptionCustom extends UsernameNotFoundException {
    private final MensajesErrorEnum mensajesErrorEnum;

    public UsernameNotFoundExceptionCustom(MensajesErrorEnum mensajesErrorEnum) {
        super(mensajesErrorEnum.getMessage());
        this.mensajesErrorEnum = mensajesErrorEnum;
    }
}
