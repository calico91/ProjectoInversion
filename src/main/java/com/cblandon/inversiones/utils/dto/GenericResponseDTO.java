package com.cblandon.inversiones.utils.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class GenericResponseDTO {
    private Integer status;
    private Object data;

    public static GenericResponseDTO genericResponse(Object data) {

        GenericResponseDTO genericResponseDto = new GenericResponseDTO();
        genericResponseDto.setStatus(HttpStatus.OK.value());
        genericResponseDto.setData(data);

        return genericResponseDto;
    }

}
