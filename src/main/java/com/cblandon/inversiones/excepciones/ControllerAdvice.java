package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.utils.MensajesErrorEnum;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<GenericResponseDTO> requestException(RequestException ex) {
        return GenericResponseDTO.genericError(
                ex.getMensajesErrorEnum(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoDataException.class)
    public ResponseEntity<GenericResponseDTO> noDataException(NoDataException ex) {
        return GenericResponseDTO.genericError(
                ex.getMensajesErrorEnum(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

        String mensaje = "Campo " + ex.getBindingResult().getFieldErrors().get(0).getField().concat(" " +
                Objects.requireNonNull(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));


        return GenericResponseDTO.genericErrorString(mensaje, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GenericResponseDTO> methodArgumentTypeMismatchException() {

        return GenericResponseDTO.genericError(MensajesErrorEnum.TIPO_DATO_INCORRECTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingPathVariableException.class)
    public ResponseEntity<GenericResponseDTO> missingPathVariableException(MissingPathVariableException ex) {

        return GenericResponseDTO.genericErrorString(
                "Se require parametro ".concat(ex.getVariableName()), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<GenericResponseDTO> illegalArgumentException() {

        return GenericResponseDTO.genericError(
                MensajesErrorEnum.ERROR_PETICION, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<GenericResponseDTO> usernameNotFoundException(UsernameNotFoundException ex) {

        return GenericResponseDTO.genericErrorString(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
