package com.cblandon.inversiones.excepciones;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.cblandon.inversiones.utils.MensajesErrorEnum;
import com.cblandon.inversiones.utils.dto.GenericResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@RestControllerAdvice
public class ControllerAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<GenericResponseDTO> requestException() {

        return GenericResponseDTO.genericError(
                MensajesErrorEnum.ERROR_NO_CONTROLADO, HttpStatus.BAD_REQUEST);
    }

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


    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<GenericResponseDTO> illegalArgumentException() {

        return GenericResponseDTO.genericError(
                MensajesErrorEnum.ERROR_PETICION, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = UsernameNotFoundExceptionCustom.class)
    public ResponseEntity<GenericResponseDTO> usernameNotFoundException(UsernameNotFoundExceptionCustom ex) {

        return GenericResponseDTO.genericError(ex.getMensajesErrorEnum(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = JWTVerificationException.class)
    public ResponseEntity<GenericResponseDTO> jWTVerificationException() {
        return GenericResponseDTO.genericError(MensajesErrorEnum.TOKEN_CADUCO, HttpStatus.BAD_REQUEST);
    }

}
