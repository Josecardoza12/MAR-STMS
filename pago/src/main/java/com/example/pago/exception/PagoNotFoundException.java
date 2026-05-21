package com.example.pago.exception;

public class PagoNotFoundException extends RuntimeException {
    public PagoNotFoundException(Long id) {
        super("Pago con id " + id + " no encontrado");
    }
}
