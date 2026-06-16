package com.example.bodega.controller;

import com.example.bodega.client.OrdenTrabajoClient;
import com.example.bodega.exception.BodegaNotFoundException;
import com.example.bodega.model.Bodega;
import com.example.bodega.model.Orden_Trabajo;
import com.example.bodega.service.BodegaService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/bodega")
public class BodegaController {

    @Autowired
    private BodegaService bodegaService;
    @Autowired
    private OrdenTrabajoClient ordenTrabajoClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<Bodega>> listarTodos() {
        log.info("GET /api/v1/bodega - Listando todos los equipos en bodega");
        List<Bodega> lista = bodegaService.listarTodos();
        if (lista.isEmpty()) {
            log.warn("No hay equipos en bodega");
            return ResponseEntity.noContent().build();
        }
        lista.forEach(b -> {
            b.add(linkTo(methodOn(BodegaController.class).listarTodos()).withRel("todos"));
        });
        log.info("Se encontraron {} equipos en bodega", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping(params = "otId")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<Bodega> obtenerPorOtId(@RequestParam Long otId) {
        log.info("GET /api/v1/bodega/ot/{} - Buscando bodega por OT", otId);
        return bodegaService.obtenerPorOtId(otId)
                .map(b -> {
                    b.add(linkTo(methodOn(BodegaController.class).obtenerPorOtId(otId)).withSelfRel());
                    b.add(linkTo(methodOn(BodegaController.class).listarTodos()).withRel("todos"));
                    log.info("Bodega encontrada para OT {}", otId);
                    return ResponseEntity.ok(b);
                })
                .orElseGet(() -> {
                    log.warn("No se encontró bodega para OT {}", otId);
                    return ResponseEntity.notFound().build();
                });
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Bodega> registrar(@Valid @RequestBody Bodega bodega,
                                            @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/bodega - Registrando equipo en bodega para OT {}", bodega.getOtId());
        ordenTrabajoClient.obtenerOt(bodega.getOtId(), token).block();
        Bodega b = bodegaService.registrar(bodega);
        log.info("Equipo registrado en bodega con id {}", b.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(b);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Bodega> actualizar(@PathVariable Long id, @RequestBody Bodega bodega,
                                             @RequestHeader("Authorization") String token) {
        log.info("PUT /api/v1/bodega/{} - Actualizando días y cobro", id);
        ordenTrabajoClient.obtenerOt(bodega.getOtId(), token).block();
        Bodega b = bodegaService.actualizar(id);
        log.info("Bodega {} actualizada - Días: {}, Monto: {}", id, b.getDiasEnBodega(), b.getMontoBodegaje());
        return ResponseEntity.ok(b);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/bodega/{} - Eliminando registro de bodega", id);
        try {
            bodegaService.eliminar(id);
            log.info("Registro de bodega {} eliminado correctamente", id);
            return ResponseEntity.noContent().build();
        } catch (BodegaNotFoundException e) {
            log.error("Bodega con id {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
    }
}