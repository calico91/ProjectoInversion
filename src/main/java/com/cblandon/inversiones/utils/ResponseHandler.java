package com.cblandon.inversiones.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseHandler {
    public ResponseEntity<Object> generateResponse(final String message, final HttpStatus httpStatus,
                                                   final Object response) {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        map.put("data", response);
        return new ResponseEntity<>(map, httpStatus);
    }

    public ResponseEntity<Object> generateResponse(final Map<String, Object> map, final HttpStatus httpStatus) {
        return new ResponseEntity<>(map, httpStatus);
    }public ResponseEntity<Object> generateResponseController(final Object object, final HttpStatus httpStatus) {
        return new ResponseEntity<>(object, httpStatus);
    }

    public ResponseEntity<Object> generateResponseWithoutData(final String message, final HttpStatus httpStatus) {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        return new ResponseEntity<>(map, httpStatus);
    }
}
