package com.example.cliente.exception;

public class ClienteNotFoundException extends  RuntimeException{
    public ClienteNotFoundException(Long id){
        super("Cliente con id " + id + " no encontrado");
    }
}
