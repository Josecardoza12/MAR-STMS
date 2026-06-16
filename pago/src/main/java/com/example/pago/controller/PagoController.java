package com.example.pago.controller;

import com.example.pago.cliente.OrdenTrabajoClient;
import com.example.pago.exception.PagoNotFoundException;
import com.example.pago.model.Pago;
import com.example.pago.service.PagoService;
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
@RequestMapping("/api/v1/pagos")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private OrdenTrabajoClient ordenTrabajoClient;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<List<Pago>> listarTodos() {
        log.info("GET /api/v1/pagos - Listando todos los pagos");
        List<Pago> lista = pagoService.listarTodos();
        if (lista.isEmpty()) {
            log.warn("No hay pagos registrados");
            return ResponseEntity.noContent().build();
        }
        lista.forEach(p -> {
            p.add(linkTo(methodOn(PagoController.class).obtenerPorId(p.getId())).withSelfRel());
            p.add(linkTo(methodOn(PagoController.class).listarTodos()).withRel("todos"));
        });
        log.info("Se encontraron {} pagos", lista.size());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        log.info("GET /api/v1/pagos/{} - Buscando pago por id", id);
        try {
            Pago p = pagoService.obtenerPorId(id);
            p.add(linkTo(methodOn(PagoController.class).obtenerPorId(id)).withSelfRel());
            p.add(linkTo(methodOn(PagoController.class).listarTodos()).withRel("todos"));
            return ResponseEntity.ok(p);
        } catch (PagoNotFoundException e) {
            log.error("Pago con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/ot/{otId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<List<Pago>> obtenerPorOtId(@PathVariable Long otId) {
        log.info("GET /api/v1/pagos/ot/{} - Buscando pagos por OT", otId);
        List<Pago> pagos = pagoService.obtenerPorOtId(otId);
        if (pagos.isEmpty()) {
            log.warn("No hay pagos para la OT {}", otId);
            return ResponseEntity.noContent().build();
        }
        pagos.forEach(p -> {
            p.add(linkTo(methodOn(PagoController.class).obtenerPorId(p.getId())).withSelfRel());
            p.add(linkTo(methodOn(PagoController.class).listarTodos()).withRel("todos"));
        });
        return ResponseEntity.ok(pagos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Pago> registrar(
            @Valid @RequestBody Pago pago,
            @RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/pagos - Registrando pago para OT {}", pago.getOtId());
        ordenTrabajoClient.obtenerOt(pago.getOtId(), token).block();
        Pago p = pagoService.registrar(pago, token);
        log.info("Pago registrado con id {}", p.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(p);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Pago> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Pago pagoActualizado,
            @RequestHeader("Authorization") String token) {
        log.info("PUT /api/v1/pagos/{} - Actualizando pago", id);
        ordenTrabajoClient.obtenerOt(pagoActualizado.getOtId(), token).block();
        try {
            return ResponseEntity.ok(pagoService.actualizar(id, pagoActualizado));
        } catch (PagoNotFoundException e) {
            log.error("Pago con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        log.info("DELETE /api/v1/pagos/{} - Eliminando pago", id);
        try {
            pagoService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (PagoNotFoundException e) {
            log.error("Pago con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }
}