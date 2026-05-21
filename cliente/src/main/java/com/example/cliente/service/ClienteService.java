package com.example.cliente.service;

import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.repository.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service

public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;


    public List<Cliente> listarClientes(){
        log.info("Listando todos los registros de clientes");
        return clienteRepository.findAll();
    }


    public Cliente obtenerPorId(Long id){
        log.info("Buscando clientes con id {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente con id {} no encontrado", id);
                    return new ClienteNotFoundException(id);
                });

    }

   ;
    public Cliente saveCliente(Cliente cliente) {
        if (cliente.getNombre() == null || cliente.getNombre().isEmpty()) {
            log.warn("No se puede registrar cliente sin nombre");
            return cliente;
        }
        if (cliente.getRut().length() < 8) {
            log.warn("El rut ingresado es inválido");
            return cliente;
        }
        log.info("Registrando cliente con rut {}", cliente.getRut());
        return clienteRepository.save(cliente);
    }



    public  String deletedCliente(Long id){
        log.warn("Eliminando cliente con id {}", id);
         clienteRepository.deleteById(id);
        log.info("Cliente {} eliminado correctamente", id);
         return "Cliente " + id + " Eliminado";
    }
}
