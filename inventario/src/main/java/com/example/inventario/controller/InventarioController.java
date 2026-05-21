package com.example.inventario.controller;

import com.example.inventario.client.ReparacionClient;
import com.example.inventario.model.Repuesto;
import com.example.inventario.service.InventarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/inventarios")
public class InventarioController {

    @Autowired
    private  InventarioService service;

    @Autowired
    private ReparacionClient reparacionClient;



    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping
    public List<Repuesto> getAll() {
        log.info("Solicitud GET /api/v1/inventarios");
        List<Repuesto> repuestos = service.findAll();
        log.info("Se encontraron {} repuestos", repuestos.size());
        return repuestos;
    }

    @PreAuthorize("hasAnyRole('CLIENTE','TECNICO','ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Repuesto> getById(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/inventarios/{}", id);
        return service.findById(id)
                .map(repuesto -> {
                    log.info("Repuesto encontrado con id {}", id);
                    return ResponseEntity.ok(repuesto);
                })
                .orElseGet(() -> {
                    log.warn("No se encontró repuesto con id {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PostMapping
    public ResponseEntity<Repuesto> create(@Valid @RequestBody Repuesto repuesto,
                                           @RequestHeader("Authorization") String token) {
        log.info("Solicitud POST /api/v1/inventarios - creando repuesto {}", repuesto.getNombre());
        reparacionClient.obtenerReparacion(repuesto.getReparacionId(), token).block();

        Repuesto guardado = service.save(repuesto);
        log.info("Repuesto creado con id {}", guardado.getRepuestoId());
        return ResponseEntity.ok(guardado);
    }

    @PreAuthorize("hasAnyRole('TECNICO','ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Repuesto> update(
            @PathVariable Long id,
            @Valid @RequestBody Repuesto repuesto,
            @RequestHeader("Authorization") String token) {

        log.info("Solicitud PUT /api/v1/inventarios/{}", id);

        reparacionClient.obtenerReparacion(repuesto.getReparacionId(), token).block();

        return service.findById(id)
                .map(existing -> {
                    log.info("Actualizando repuesto con id {}", id);
                    repuesto.setRepuestoId(id);
                    Repuesto actualizado = service.save(repuesto);
                    log.info("Repuesto {} actualizado correctamente", id);
                    return ResponseEntity.ok(actualizado);
                })
                .orElseGet(() -> {
                    log.warn("No se encontró repuesto con id {} para actualizar", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("Solicitud DELETE /api/v1/inventarios/{}", id);
        service.delete(id);
        log.info("Repuesto con id {} eliminado", id);
        return ResponseEntity.noContent().build();
    }
}
