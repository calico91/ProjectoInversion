package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.excepciones.dto.ErrorDTO;
import com.cblandon.inversiones.excepciones.request_exception.RequestException;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ControllerAdvice {



    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<ErrorDTO> requestException(RequestException ex) {
        ErrorDTO error = ErrorDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getRequestExceptionMensajes().getMessage())
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
    public ResponseEntity<Object> methodArgumentTypeMismatchException() {

        return new ResponseHandler().generateResponseError(
                Constantes.ERROR, HttpStatus.BAD_REQUEST, "El tipo de dato enviado es incorrecto");
    }

    @ExceptionHandler(value = MissingPathVariableException.class)
    public ResponseEntity<Object> missingPathVariableException(MissingPathVariableException ex) {

        return new ResponseHandler().generateResponseError(
                Constantes.ERROR, HttpStatus.BAD_REQUEST, "Se require parametro ".concat(ex.getVariableName()));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<Object> illegalArgumentException() {

        return new ResponseHandler().generateResponseError(
                Constantes.ERROR, HttpStatus.BAD_REQUEST, "Error con la informacion suministrada");
    }



}
