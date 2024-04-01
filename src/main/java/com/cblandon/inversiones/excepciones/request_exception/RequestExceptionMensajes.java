package com.cblandon.inversiones.excepciones.request_exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RequestExceptionMensajes {

    USUARIO_REGISTRADO(
            "Usuario registrado, verifique el nombre de usuario que se encuentra creando"),
    ERROR_FECHA_NUEVA(
            "La fecha nueva no puede ser menor a la fecha de la cuota"),
    NO_PUEDE_PAGAR_CUOTA_NORMAL(
            "No puede pagar la cuota normal, realice el pago por modulo de credito"),
    CUOTA_YA_PAGADA(
            "Cuota ya se encuentra cancelada, verifique la informacion"),
    DOCUMENTO_DUPLICADO(
            "El documento ya se encuentra registrado"),
    CLIENTE_NO_CREADO(
            "El cliente no se encuentra creado"),
    ERROR_FECHAS_CREDITO(
            "Fecha credito no puede ser mayor o igual a fecha cuota"),
    ROL_NO_ENCONTRADO(
            "El rol no existe");


    private final String message;
}


