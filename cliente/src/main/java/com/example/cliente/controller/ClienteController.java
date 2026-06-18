package com.example.cliente.controller;

import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/clientes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Clientes" , description = "Gestión de clientes registrados en el sistema de MAR-STMS")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Obtener clientes",
            description = "Obtiene la lista completa de clientes registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    Cliente.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Clientes no encontrados")
    })
    public ResponseEntity<List<Cliente>> listarClientes(){
        log.info("Solicitud GET /api/v1/clientes");
        List<Cliente> clientes = clienteService.listarClientes();
        if(clientes.isEmpty()){
            log.info("No hay clientes registrados");
            return ResponseEntity.noContent().build();
        }
        for (Cliente  cliente : clientes){
            cliente.add(linkTo(methodOn(ClienteController.class).obtenerPorId(cliente.getId())).withSelfRel());
            cliente.add(linkTo(methodOn(ClienteController.class).eliminar(cliente.getId())).withRel("eliminar"));

        }
        log.info("Se encontraron {} clientes", clientes.size());
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Obtener cliente por ID",
            description = "Busca y retorna la información de un cliente según su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado,operación exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    Cliente.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "ID de cliente no encontrado")
    })
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/clientes/{}", id);
        Cliente cliente = clienteService.obtenerPorId(id);
        cliente.add(linkTo(methodOn(ClienteController.class).listarClientes()).withRel("Clientes"));
        cliente.add(linkTo(methodOn(ClienteController.class).eliminar(id)).withRel("eliminar"));

        log.info("Cliente encontrado con id {}", id);
        return ResponseEntity.ok(cliente);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @Operation(
            summary = "Crear clientes",
            description = "Crear un nuevo cliente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    Cliente.class)))
    })

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
    @Operation(
            summary = "Actualizar clientes",
            description = "Actualiza un  cliente existente"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    Cliente.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado")
    })
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
    @Operation(
            summary = "Eliminar un cliente.",
            description = "Elimina un cliente por su id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
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
