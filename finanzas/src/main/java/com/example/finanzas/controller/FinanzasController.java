package com.example.finanzas.controller;

import com.example.finanzas.client.PagoClient;
import com.example.finanzas.exception.FinanzasNotFoundException;
import com.example.finanzas.model.Finanzas;
import com.example.finanzas.service.FinanzasService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/finanzas")
public class FinanzasController {

    @Autowired
    private FinanzasService finanzasService;
    @Autowired
    private PagoClient pagoClient;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Finanzas>> listarTodos() {
        List<Finanzas> lista = finanzasService.listarTodos();
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Finanzas> obtenerPorId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(finanzasService.obtenerPorId(id));
        } catch (FinanzasNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(params = "PagoId")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Finanzas>> listarPorOtId(@RequestParam Long PagoId) {
        List<Finanzas> lista = finanzasService.listarPorPagoId(PagoId);
        if (lista.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Finanzas> registrar(@Valid @RequestBody Finanzas gasto,@RequestHeader("Authorization") String token) {
        log.info("POST /api/v1/gastos - Registrando gasto categoria {}", gasto.getCategoria());
        pagoClient.obtenerPago(gasto.getPagoId(),token).block();

        Finanzas g = finanzasService.registrar(gasto);
        return ResponseEntity.status(HttpStatus.CREATED).body(g);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Finanzas> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Finanzas finanzasActualizado,
            @RequestHeader("Authorization") String token) {
        log.info("PUT /api/v1/finanzas/{} - Actualizando finanza", id);
        pagoClient.obtenerPago(finanzasActualizado.getPagoId(),token).block();
        try {
            return ResponseEntity.ok(finanzasService.actualizar(id, finanzasActualizado));
        } catch (FinanzasNotFoundException e) {
            log.error("Finanzas con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        try {
            finanzasService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (FinanzasNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/total")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Double>> total() {
        return ResponseEntity.ok(Map.of(
                "totalGastos", finanzasService.totalGastos()
        ));
    }
}