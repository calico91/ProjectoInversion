package com.cblandon.inversiones.utils.dto;

import com.cblandon.inversiones.utils.Constantes;
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

    public static ResponseEntity<GenericResponseDTO> genericError(String mensajeError, int httpStatus) {

        GenericResponseDTO genericErrorDto = new GenericResponseDTO();
        genericErrorDto.setData("error");
        genericErrorDto.setStatus(httpStatus);
        genericErrorDto.setMessage(mensajeError);

        return new ResponseEntity<>(genericErrorDto, HttpStatus.OK);
    }

    public static GenericResponseDTO genericResponseLogin(Object data) {

        GenericResponseDTO genericResponseDto = new GenericResponseDTO();
        genericResponseDto.setStatus(HttpStatus.OK.value());
        genericResponseDto.setMessage(Constantes.SUCCESSFUL);
        genericResponseDto.setData(data);

        return genericResponseDto;
    }


}
