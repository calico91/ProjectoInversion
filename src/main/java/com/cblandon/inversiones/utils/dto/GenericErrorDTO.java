package com.cblandon.inversiones.utils.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GenericErrorDTO {
    private Integer status;
    private String message;

    public static GenericErrorDTO genericError(String message, int status) {

        GenericErrorDTO genericErrorDto = new GenericErrorDTO();
        genericErrorDto.setStatus(status);
        genericErrorDto.setMessage(message);

        return genericErrorDto;
    }

}
