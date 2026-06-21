package com.example.inventario.controller;

import com.example.inventario.client.ReparacionClient;
import com.example.inventario.model.Repuesto;
import com.example.inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/inventarios")
@SecurityRequirement(name = "bearerAuth")
public class InventarioController {

    private final InventarioService service;
    private final ReparacionClient reparacionClient;

    public InventarioController(InventarioService service, ReparacionClient reparacionClient) {
        this.service = service;
        this.reparacionClient = reparacionClient;
    }

    @Operation(summary = "Listar todos los repuestos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping
    public ResponseEntity<List<Repuesto>> getAll() {
        log.info("GET /api/v1/inventarios");

        List<Repuesto> repuestos = service.findAll();

        repuestos.forEach(r -> {
            r.add(linkTo(methodOn(InventarioController.class)
                    .getById(r.getRepuestoId()))
                    .withRel("self"));

            r.add(linkTo(methodOn(InventarioController.class)
                    .delete(r.getRepuestoId()))
                    .withRel("eliminar"));
        });

        return ResponseEntity.ok(repuestos);
    }

    @Operation(summary = "Obtener repuesto por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Repuesto encontrado"),
            @ApiResponse(responseCode = "404", description = "Repuesto no existe")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Repuesto> getById(@PathVariable Long id) {
        log.info("GET /api/v1/inventarios/{}", id);

        return service.findById(id)
                .map(r -> {
                    r.add(linkTo(methodOn(InventarioController.class)
                            .getById(id))
                            .withRel("self"));

                    r.add(linkTo(methodOn(InventarioController.class)
                            .delete(id))
                            .withRel("eliminar"));

                    r.add(linkTo(methodOn(InventarioController.class)
                            .getAll())
                            .withRel("todos"));

                    return ResponseEntity.ok(r);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear repuesto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Repuesto creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PostMapping
    public ResponseEntity<Repuesto> create(
            @Valid @RequestBody Repuesto repuesto,
            @RequestHeader("Authorization") String token) {

        log.info("POST /api/v1/inventarios");

        reparacionClient.obtenerReparacion(repuesto.getReparacionId(), token).block();

        Repuesto guardado = service.save(repuesto);

        guardado.add(linkTo(methodOn(InventarioController.class)
                .getById(guardado.getRepuestoId()))
                .withRel("self"));

        guardado.add(linkTo(methodOn(InventarioController.class)
                .getAll())
                .withRel("todos"));

        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @Operation(summary = "Actualizar repuesto")
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Repuesto> update(
            @PathVariable Long id,
            @Valid @RequestBody Repuesto repuesto,
            @RequestHeader("Authorization") String token) {

        log.info("PUT /api/v1/inventarios/{}", id);

        reparacionClient.obtenerReparacion(repuesto.getReparacionId(), token).block();

        return service.findById(id)
                .map(existing -> {
                    repuesto.setRepuestoId(id);
                    Repuesto actualizado = service.save(repuesto);

                    actualizado.add(linkTo(methodOn(InventarioController.class)
                            .getById(id))
                            .withRel("self"));

                    actualizado.add(linkTo(methodOn(InventarioController.class)
                            .getAll())
                            .withRel("todos"));

                    actualizado.add(linkTo(methodOn(InventarioController.class)
                            .delete(id))
                            .withRel("eliminar"));

                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar repuesto")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE /api/v1/inventarios/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
