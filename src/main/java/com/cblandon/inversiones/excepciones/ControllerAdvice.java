package com.cblandon.inversiones.excepciones;

import com.cblandon.inversiones.excepciones.request_exception.RequestException;
import com.cblandon.inversiones.utils.Constantes;
import com.cblandon.inversiones.utils.ResponseHandler;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<GenericResponseDTO> requestException(RequestException ex) {
        return GenericResponseDTO.genericError(
                ex.getRequestExceptionMensajes().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = NoDataException.class)
    public ResponseEntity<GenericResponseDTO> noDataException(NoDataException ex) {
        return GenericResponseDTO.genericError(
                ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<GenericResponseDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        /*
        Map<String, Object> error = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(
                errors -> error.put(errors.getField(), errors.getDefaultMessage()));*/
        String mensaje = "Campo " + ex.getBindingResult().getFieldErrors().get(0).getField().concat(" " +
                Objects.requireNonNull(ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage()));


        return GenericResponseDTO.genericError(mensaje, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(value = UsernameNotFoundException.class)
    public ResponseEntity<GenericResponseDTO> usernameNotFoundException(UsernameNotFoundException ex) {

        return GenericResponseDTO.genericError(
                ex.getMessage(), HttpStatus.BAD_REQUEST);
    }


}
