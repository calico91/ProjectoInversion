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
    }public ResponseEntity<Object> generateResponseError(final String message, final HttpStatus httpStatus,
                                                   final Object response) {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        map.put("error", response);
        return new ResponseEntity<>(map, httpStatus);
    }


    public ResponseEntity<Object> generateResponseWithoutData(final String message, final HttpStatus httpStatus) {
        final Map<String, Object> map = new HashMap<>();
        map.put("message", message);
        map.put("status", httpStatus.value());
        return new ResponseEntity<>(map, httpStatus);
    }
}
