package com.example.equipo.exception;

public class EquipoNotFoundException extends RuntimeException{
    public EquipoNotFoundException(Long id){
        super("Equipo con id " + id + " no encontrado");
    }
}
