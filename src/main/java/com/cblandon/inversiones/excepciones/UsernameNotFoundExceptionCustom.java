package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Getter
public class UsernameNotFoundExceptionCustom extends UsernameNotFoundException {
    private final MensajesErrorEnum mensajesErrorEnum;

    public UsernameNotFoundExceptionCustom(String msg, MensajesErrorEnum mensajesErrorEnum) {
        super(msg);
        this.mensajesErrorEnum = mensajesErrorEnum;
    }
}
