package com.example.reparacion.controller;

import com.example.reparacion.client.OrdenTrabajoClient;
import com.example.reparacion.model.Reparacion;
import com.example.reparacion.service.ReparacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/reparaciones")
@RequiredArgsConstructor
public class ReparacionController {

    private final ReparacionService service;
    @Autowired
    private OrdenTrabajoClient ordenTrabajoClient;

    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping
    public List<Reparacion> getAll() {
        log.info("GET api/v1/reparaciones");
        return service.findAll();
    }

    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping("/{id}")
    public Reparacion getById(@PathVariable Long id) {
        log.info("GET api/v1/reparaciones/{}", id);
        return service.findById(id);
    }
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping(params = "otId")
    public Reparacion getByOtId(@RequestParam Long otId) {
        log.info("GET api/v1/Reparacion/ot/{}", otId);
        return service.findByOtId(otId);
    }

    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @PostMapping
    public Reparacion create(
            @Valid @RequestBody Reparacion reparacion,
            @RequestHeader("Authorization") String token) {

        ordenTrabajoClient.obtenerOt(reparacion.getOtId(), token).block();
        log.info("POST api/v1/reparaciones");
        return service.save(reparacion, token);
    }

    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @PutMapping("/{id}")
    public Reparacion update(
            @PathVariable Long id,
            @Valid @RequestBody Reparacion reparacion,
            @RequestHeader("Authorization") String token) {

        ordenTrabajoClient.obtenerOt(reparacion.getOtId(), token).block();

        log.info("PUT api/v1/reparaciones/{}", id);
        return service.update(id, reparacion, token);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE api/v1/reparaciones/{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
