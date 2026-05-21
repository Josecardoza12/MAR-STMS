package com.example.diagnostico.exception;

public class DiagnosticoNotFoundException extends RuntimeException {
    public DiagnosticoNotFoundException(Long id) {
        super("Diagnóstico con id " + id + " no encontrado");
    }
}

