package com.example.bodega.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BodegaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(BodegaNotFoundException ex) {
        HashMap<String, String> errores = new HashMap<>();
        errores.put("id", ex.getMessage());
        return new ErrorResponse(404, "Bodega no encontrada", errores);
    }
}
