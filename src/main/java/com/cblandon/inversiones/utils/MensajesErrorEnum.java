package com.cblandon.inversiones.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MensajesErrorEnum {

    USUARIO_REGISTRADO("Usuario registrado, verifique el nombre de usuario que se encuentra creando", "E1"),
    ERROR_FECHA_NUEVA("La fecha nueva no puede ser menor a la fecha de la cuota", "E2"),
    NO_PUEDE_PAGAR_CUOTA_NORMAL("No puede pagar la cuota normal, realice el pago por modulo de credito", "E3"),
    CUOTA_YA_PAGADA("Cuota ya se encuentra cancelada, verifique la informacion", "E4"),
    DOCUMENTO_DUPLICADO("El documento ya se encuentra registrado", "E5"),
    CLIENTE_NO_CREADO("El cliente no se encuentra creado", "E6"),
    ERROR_FECHAS_CREDITO("Fecha credito no puede ser mayor o igual a fecha cuota", "E7"),
    ROL_NO_ENCONTRADO("El rol no existe", "E8"),
    TIPO_DATO_INCORRECTO("El tipo de dato enviado es incorrecto", "E9"),
    ERROR_PETICION("Error con la informacion suministrada", "E10"),
    DATOS_NO_ENCONTRADOS("No se encontraron resultados con los parametros de busqueda ingresados", "E11"),
    TOKEN_CADUCO("Token caduco, inicie sesion de nuevo", "E12");


    private final String message;
    private final String codigo;
}


