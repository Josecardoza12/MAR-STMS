package com.example.diagnostico.controller;

import com.example.diagnostico.client.OrdenTrabajoClient;
import com.example.diagnostico.model.Diagnostico;
import com.example.diagnostico.service.DiagnosticoService;
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
@RequestMapping("api/v1/diagnosticos")
@RequiredArgsConstructor

public class DiagnosticoController {

    @Autowired
    private  DiagnosticoService service;
    @Autowired
    private  OrdenTrabajoClient ordenTrabajoClient;

    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping
    public List<Diagnostico> getAll() {
        log.info("GET api/v1/diagnosticos");
        return service.findAll();
    }

    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping("/{id}")
    public Diagnostico getById(@PathVariable Long id) {
        log.info("GET api/v1/diagnosticos{}", id);
        return service.findById(id);
    }

    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @GetMapping(params = "otId")
    public Diagnostico getByOtId(@RequestParam Long otId) {
        log.info("GET api/v1/diagnosticos/ot/{}", otId);
        return service.findByOtId(otId);
    }

    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @PostMapping
    public Diagnostico create(
            @Valid @RequestBody Diagnostico diagnostico,
            @RequestHeader("Authorization") String token) {

        log.info("POST api/v1/diagnosticos");
        ordenTrabajoClient.obtenerOt(diagnostico.getOtId(), token).block();
        return service.save(diagnostico, token);
    }

    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @PutMapping("/{id}")
    public Diagnostico update(
            @PathVariable Long id,
            @Valid @RequestBody Diagnostico diagnostico,
            @RequestHeader("Authorization") String token) {

        log.info("PUT api/v1/diagnosticos{}", id);
        ordenTrabajoClient.obtenerOt(diagnostico.getOtId(), token).block();

        return service.update(id, diagnostico, token);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.warn("DELETE api/v1/diagnosticos{}", id);
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
