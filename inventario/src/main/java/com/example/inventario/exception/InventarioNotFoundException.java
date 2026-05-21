package com.example.inventario.exception;

public class InventarioNotFoundException extends RuntimeException {
    public InventarioNotFoundException(Long id) {
        super("Inventario con id " + id + " no encontrado");
    }
}

