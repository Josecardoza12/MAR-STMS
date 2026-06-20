package com.example.diagnostico.controller;

import com.example.diagnostico.client.OrdenTrabajoClient;
import com.example.diagnostico.model.Diagnostico;
import com.example.diagnostico.service.DiagnosticoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("api/v1/diagnosticos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DiagnosticoController {

    private final DiagnosticoService service;
    private final OrdenTrabajoClient ordenTrabajoClient;

    @Operation(summary = "Listar todos los diagnósticos")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping
    public ResponseEntity<List<Diagnostico>> getAll() {
        log.info("GET api/v1/diagnosticos");

        List<Diagnostico> lista = service.findAll();

        lista.forEach(d -> {
            d.add(linkTo(methodOn(DiagnosticoController.class)
                    .getById(d.getDiagnosticoId()))
                    .withRel("self"));
        });

        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtener diagnóstico por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Diagnóstico encontrado"),
            @ApiResponse(responseCode = "404", description = "Diagnóstico no existe")
    })
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Diagnostico> getById(@PathVariable Long id) {
        log.info("GET api/v1/diagnosticos/{}", id);

        Diagnostico d = service.findById(id);

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getById(id))
                .withRel("self"));

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getAll())
                .withRel("todos"));

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .delete(id))
                .withRel("eliminar"));

        return ResponseEntity.ok(d);
    }

    @Operation(summary = "Obtener diagnóstico por OT")
    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping(params = "otId")
    public ResponseEntity<Diagnostico> getByOtId(@RequestParam Long otId) {
        log.info("GET api/v1/diagnosticos?otId={}", otId);

        Diagnostico d = service.findByOtId(otId);

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getById(d.getDiagnosticoId()))
                .withRel("self"));

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getAll())
                .withRel("todos"));

        return ResponseEntity.ok(d);
    }

    @Operation(summary = "Crear diagnóstico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Diagnóstico creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PostMapping
    public ResponseEntity<Diagnostico> create(
            @Valid @RequestBody Diagnostico diagnostico,
            @RequestHeader("Authorization") String token) {

        log.info("POST api/v1/diagnosticos");

        ordenTrabajoClient.obtenerOt(diagnostico.getOtId(), token).block();

        Diagnostico d = service.save(diagnostico, token);

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getById(d.getDiagnosticoId()))
                .withRel("self"));

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getAll())
                .withRel("todos"));

        return ResponseEntity.status(HttpStatus.CREATED).body(d);
    }

    @Operation(summary = "Actualizar diagnóstico")
    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Diagnostico> update(
            @PathVariable Long id,
            @Valid @RequestBody Diagnostico diagnostico,
            @RequestHeader("Authorization") String token) {

        log.info("PUT api/v1/diagnosticos/{}", id);

        ordenTrabajoClient.obtenerOt(diagnostico.getOtId(), token).block();

        Diagnostico d = service.update(id, diagnostico, token);

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getById(id))
                .withRel("self"));

        d.add(linkTo(methodOn(DiagnosticoController.class)
                .getAll())
                .withRel("todos"));

        return ResponseEntity.ok(d);
    }

    @Operation(summary = "Eliminar diagnóstico")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE api/v1/diagnosticos/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
