package com.example.orden_trabajo.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
@Data
public class ErrorResponse {
    private int status;
    private String error;
    private LocalDateTime timestamp;
    HashMap<String, String> errores;

    public ErrorResponse(int status, String error, HashMap<String, String> errores) {
        this.status = status;
        this.errores = errores;
        this.error = error;
        this.timestamp = LocalDateTime.now();


    }
}
