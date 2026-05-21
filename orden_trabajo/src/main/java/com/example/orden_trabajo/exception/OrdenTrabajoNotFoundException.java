package com.example.orden_trabajo.exception;

public class OrdenTrabajoNotFoundException extends RuntimeException{
    public OrdenTrabajoNotFoundException(Long id){
        super("Orden de trabajo con id " + id + " no encontrado");
    }

}
