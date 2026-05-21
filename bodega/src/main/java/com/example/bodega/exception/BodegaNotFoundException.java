package com.example.bodega.exception;

public class BodegaNotFoundException extends RuntimeException {
    public BodegaNotFoundException(Long id) {
        super("Bodega con id " + id + " no encontrada");
    }
}
