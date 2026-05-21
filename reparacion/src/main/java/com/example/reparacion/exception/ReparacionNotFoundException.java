package com.example.reparacion.exception;

public class ReparacionNotFoundException extends RuntimeException {
    public ReparacionNotFoundException(Long id) {
        super("Reparación con id " + id + " no encontrada");
    }
}

