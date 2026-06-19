package com.example.reparacion.controller;

import com.example.reparacion.client.OrdenTrabajoClient;
import com.example.reparacion.model.Reparacion;
import com.example.reparacion.service.ReparacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("api/v1/reparaciones")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ReparacionController {

    private final ReparacionService service;
    private final OrdenTrabajoClient ordenTrabajoClient;

    @Operation(summary = "Listar todas las reparaciones")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping
    public ResponseEntity<CollectionModel<Reparacion>> getAll() {
        log.info("GET api/v1/reparaciones");
        List<Reparacion> lista = service.findAll();

        lista.forEach(r -> {
            r.add(linkTo(methodOn(ReparacionController.class).getById(r.getReparacionId())).withSelfRel());
            r.add(linkTo(methodOn(ReparacionController.class).delete(r.getReparacionId())).withRel("eliminar"));
            r.add(linkTo(methodOn(ReparacionController.class).getByOtId(r.getOtId())).withRel("OT"));
        });

        return ResponseEntity.ok(CollectionModel.of(lista));
    }

    @Operation(summary = "Obtener reparación por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reparación encontrada"),
            @ApiResponse(responseCode = "404", description = "Reparación no existe")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Reparacion> getById(@PathVariable Long id) {
        log.info("GET api/v1/reparaciones/{}", id);
        Reparacion r = service.findById(id);

        r.add(linkTo(methodOn(ReparacionController.class).getAll()).withRel("todos"));
        r.add(linkTo(methodOn(ReparacionController.class).update(id, r, null)).withRel("update"));
        r.add(linkTo(methodOn(ReparacionController.class).delete(id)).withRel("eliminar"));
        r.add(linkTo(methodOn(ReparacionController.class).getByOtId(r.getOtId())).withRel("OT"));

        return ResponseEntity.ok(r);
    }

    @Operation(summary = "Obtener reparación por OT")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping(params = "otId")
    public ResponseEntity<Reparacion> getByOtId(@RequestParam Long otId) {
        log.info("GET api/v1/reparaciones?otId={}", otId);
        Reparacion r = service.findByOtId(otId);

        r.add(linkTo(methodOn(ReparacionController.class).getById(r.getReparacionId())).withSelfRel());
        r.add(linkTo(methodOn(ReparacionController.class).getAll()).withRel("todos"));
        r.add(linkTo(methodOn(ReparacionController.class).delete(r.getReparacionId())).withRel("eliminar"));

        return ResponseEntity.ok(r);
    }

    @Operation(summary = "Crear reparación")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Reparación creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PostMapping
    public ResponseEntity<Reparacion> create(
            @Valid @RequestBody Reparacion reparacion,
            @RequestHeader("Authorization") String token) {

        log.info("POST api/v1/reparaciones");
        ordenTrabajoClient.obtenerOt(reparacion.getOtId(), token).block();

        Reparacion r = service.save(reparacion, token);

        r.add(linkTo(methodOn(ReparacionController.class).getById(r.getReparacionId())).withSelfRel());
        r.add(linkTo(methodOn(ReparacionController.class).getAll()).withRel("todos"));
        r.add(linkTo(methodOn(ReparacionController.class).delete(r.getReparacionId())).withRel("eliminar"));

        return ResponseEntity.status(HttpStatus.CREATED).body(r);
    }

    @Operation(summary = "Actualizar reparación")
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Reparacion> update(
            @PathVariable Long id,
            @Valid @RequestBody Reparacion reparacion,
            @RequestHeader("Authorization") String token) {

        log.info("PUT api/v1/reparaciones/{}", id);
        ordenTrabajoClient.obtenerOt(reparacion.getOtId(), token).block();

        Reparacion r = service.update(id, reparacion, token);

        r.add(linkTo(methodOn(ReparacionController.class).getById(id)).withSelfRel());
        r.add(linkTo(methodOn(ReparacionController.class).getAll()).withRel("todos"));
        r.add(linkTo(methodOn(ReparacionController.class).delete(id)).withRel("eliminar"));

        return ResponseEntity.ok(r);
    }

    @Operation(summary = "Eliminar reparación")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE api/v1/reparaciones/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
