package com.example.finanzas.exception;

public class FinanzasNotFoundException extends RuntimeException {
    public FinanzasNotFoundException(Long id) {
        super("finanzas con id " + id + " no encontrado");
    }
}