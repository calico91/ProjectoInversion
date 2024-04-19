package com.cblandon.inversiones.utils.dto;

import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
public class GenericResponseDTO {
    private Integer status;
    private Object data;
    private String message;

    public static ResponseEntity<GenericResponseDTO> genericResponse(Object data) {

        GenericResponseDTO genericResponseDto = new GenericResponseDTO();
        genericResponseDto.setStatus(HttpStatus.OK.value());
        genericResponseDto.setMessage(Constantes.SUCCESSFUL);
        genericResponseDto.setData(data);

        return new ResponseEntity<>(genericResponseDto, HttpStatus.OK);
    }

    public static ResponseEntity<GenericResponseDTO> genericError(
            MensajesErrorEnum mensajeError, HttpStatus httpStatus) {

        GenericResponseDTO genericErrorDto = new GenericResponseDTO();
        genericErrorDto.setData(mensajeError.getCodigo());
        genericErrorDto.setStatus(httpStatus.value());
        genericErrorDto.setMessage(mensajeError.getMessage());

        return new ResponseEntity<>(genericErrorDto, httpStatus);
    }

    public static GenericResponseDTO genericResponseLogin(Object data) {

        GenericResponseDTO genericResponseDto = new GenericResponseDTO();
        genericResponseDto.setStatus(HttpStatus.OK.value());
        genericResponseDto.setMessage(Constantes.SUCCESSFUL);
        genericResponseDto.setData(data);

        return genericResponseDto;
    }

    public static ResponseEntity<GenericResponseDTO> genericErrorString(
            String mensajeError, HttpStatus httpStatus) {

        GenericResponseDTO genericErrorDto = new GenericResponseDTO();
        genericErrorDto.setData("E500");
        genericErrorDto.setStatus(httpStatus.value());
        genericErrorDto.setMessage(mensajeError);

        return new ResponseEntity<>(genericErrorDto, httpStatus);
    }

}
