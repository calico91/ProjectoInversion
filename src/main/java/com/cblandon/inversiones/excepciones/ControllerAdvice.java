package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.excepciones.dto.ErrorDTO;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ErrorDTO> runtimeExeceptionHandler(RuntimeException ex) {
        ErrorDTO error = ErrorDTO.builder().status(1).message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> requestException(RequestException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(ex.getStatus())
                .message(ex.getMessage().toString())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoDataException.class)
    public ResponseEntity<ErrorDTO> noDataException(NoDataException ex) {
        ErrorDTO error = ErrorDTO.builder().status(ex.getStatus()).message(ex.getMessage()).build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                errors -> error.put(errors.getField(), errors.getDefaultMessage()));
        return new ResponseHandler().generateResponseError(
                Constantes.ERROR, HttpStatus.BAD_REQUEST, error);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> runtimeExeceptionHandler() {

        return new ResponseHandler().generateResponseError(
                Constantes.ERROR, HttpStatus.BAD_REQUEST, "El tipo de dato enviado es incorrecto");
    }
}
