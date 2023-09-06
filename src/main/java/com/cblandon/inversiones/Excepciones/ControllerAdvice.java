package com.cblandon.inversiones.Excepciones;

import com.cblandon.inversiones.Excepciones.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDTO> runtimeExeceptionHandler(RuntimeException ex) {
        ErrorDTO error = ErrorDTO.builder().code("1").message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> requestException(RuntimeException ex) {
        ErrorDTO error = ErrorDTO.builder().code("2").message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoDataException.class)
    public ResponseEntity<ErrorDTO> noDataException(RuntimeException ex) {
        ErrorDTO error = ErrorDTO.builder().code("3").message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NO_CONTENT);
    }
}
