package com.example.cliente.controller;

import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.service.ClienteService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;



    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<List<Cliente>> listarClientes(){
        log.info("Solicitud GET /api/v1/clientes");
        List<Cliente> clientes = clienteService.listarClientes();
        if(clientes.isEmpty()){
            log.info("No hay clientes registrados");
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} clientes", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/clientes/{}", id);
        Cliente cliente = clienteService.obtenerPorId(id);
        log.info("Cliente encontrado con id {}", id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<Cliente> crear(@Valid @RequestBody Cliente cliente){
        log.info("Solicitud POST /api/v1/clientes - Creando cliente con rut {}", cliente.getRut());
        Cliente c = clienteService.saveCliente(cliente);
        if(c.getId() == null){
            log.warn("No se pudo crear el cliente: nombre vacío o rut inválido");
            return ResponseEntity.badRequest().build();
        }
        log.info("Cliente creado correctamente con id {}", c.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<Cliente> actualizar(@PathVariable Long id, @Valid @RequestBody Cliente clienteActualizado) {
        log.info("Solicitud PUT /api/v1/clientes/{}", id);
        try {
            Cliente c = clienteService.obtenerPorId(id);

            c.setNombre(clienteActualizado.getNombre());
            c.setRut(clienteActualizado.getRut());
            c.setTelefono(clienteActualizado.getTelefono());
            c.setDireccion(clienteActualizado.getDireccion());
            c.setTipo_Cliente(clienteActualizado.getTipo_Cliente());

            Cliente actualizado = clienteService.saveCliente(c);
            if(actualizado.getId() == null){
                log.warn("No se pudo actualizar el cliente: nombre vacío o rut inválido");
                return ResponseEntity.badRequest().build();
            }
            log.info("Cliente {} actualizado correctamente", id);
            return ResponseEntity.ok(actualizado);
        } catch (ClienteNotFoundException e) {
            log.error("Error al actualizar: cliente con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }


    @PreAuthorize("hasAnyRole('ADMIN')")

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        log.warn("Solicitud DELETE /api/v1/clientes/{}", id);
        try{
            clienteService.deletedCliente(id);
            log.info("Cliente {} eliminado correctamente", id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            log.error("Error al eliminar cliente con id {}", id);
            return ResponseEntity.notFound().build();
        }
    }



}
