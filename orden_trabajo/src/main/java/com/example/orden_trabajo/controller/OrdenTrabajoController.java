package com.example.orden_trabajo.controller;

import com.example.orden_trabajo.client.EquipoWebClient;
import com.example.orden_trabajo.client.clienteWebClient;
import com.example.orden_trabajo.exception.OrdenTrabajoNotFoundException;
import com.example.orden_trabajo.model.OrdenTrabajo;
import com.example.orden_trabajo.service.OrdenTrabajoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/ot")
public class OrdenTrabajoController {

    @Autowired
    private clienteWebClient clienteWebClient;

    @Autowired
    private EquipoWebClient equipoWebClient;

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<List<OrdenTrabajo>> ListarOt() {
        log.info("Solicitud GET /api/v1/ot");
        List<OrdenTrabajo> ot = ordenTrabajoService.listarOt();
        if (ot.isEmpty()) {
            log.info("No hay orden de trabajo registradas");
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} orden de trabajo", ot.size());
        return ResponseEntity.ok(ot);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<OrdenTrabajo> obtenerOtPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/ot/{}", id);
        OrdenTrabajo ot = ordenTrabajoService.obtenerOtPorId(id);
        log.info("Orden de trabajo encontrada con id {}", id);
        return ResponseEntity.ok(ot);
    }

    @GetMapping(params = "idCliente")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<List<OrdenTrabajo>> findByIdCliente(@RequestParam Long idCliente) {
        log.info("Solicitud GET /api/v1/ot?idCliente={}", idCliente);
        List<OrdenTrabajo> clienteOt = ordenTrabajoService.findByClienteId(idCliente);
        if (clienteOt.isEmpty()) {
            log.info("No hay orden de trabajo para el cliente {}", idCliente);
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} orden de trabajo para el cliente {}", clienteOt.size(), idCliente);
        return ResponseEntity.ok(clienteOt);
    }

    @GetMapping(params = "idEquipo")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    public ResponseEntity<List<OrdenTrabajo>> findByIdEquipo(@RequestParam Long idEquipo) {
        log.info("Solicitud GET /api/v1/ot?idEquipo={}", idEquipo);
        List<OrdenTrabajo> equipoOt = ordenTrabajoService.findByIdEquipo(idEquipo);
        if (equipoOt.isEmpty()) {
            log.info("No hay orden de trabajo para el equipo {}", idEquipo);
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} orden de trabajo para el equipo {}", equipoOt.size(), idEquipo);
        return ResponseEntity.ok(equipoOt);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<OrdenTrabajo> crearOt(@Valid @RequestBody OrdenTrabajo ot,
                                                @RequestHeader("Authorization") String token) {
        log.info("Solicitud POST /api/v1/ot - creando OT para cliente {} y equipo {}",
                ot.getIdCliente(), ot.getIdEquipo());
        clienteWebClient.obtenerCliente(ot.getIdCliente(), token).block();
        log.info("Cliente {} validado correctamente", ot.getIdCliente());

        equipoWebClient.obtenerEquipo(ot.getIdEquipo(), token).block();
        log.info("Equipo {} validado correctamente", ot.getIdEquipo());

        log.info("Intentando guardar OT estado {} pago {}", ot.getEstado(), ot.getEstadoPago());
        OrdenTrabajo guardado = ordenTrabajoService.saveOt(ot);
        if (guardado.getId() == null) {
            log.warn("No se pudo crear la OT: no puede quedar ENTREGADA sin estar PAGADA");
            return ResponseEntity.badRequest().build();
        }
        log.info("OT creada correctamente con id {}", guardado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    public ResponseEntity<OrdenTrabajo> actualizarOt(@PathVariable Long id, @Valid @RequestBody OrdenTrabajo otActualizada,
                                                     @RequestHeader("Authorization") String token) {
        log.info("Solicitud PUT /api/v1/ot/{}", id);

        try {
            clienteWebClient.obtenerCliente(otActualizada.getIdCliente(), token).block();

            equipoWebClient.obtenerEquipo(otActualizada.getIdEquipo(), token).block();
            OrdenTrabajo ot = ordenTrabajoService.obtenerOtPorId(id);
            ot.setEstado(otActualizada.getEstado());
            ot.setFechaEntrega(otActualizada.getFechaEntrega());
            ot.setDiagnosticoMonto(otActualizada.getDiagnosticoMonto());
            ot.setRepuestosMonto(otActualizada.getRepuestosMonto());
            ot.setManoObraMonto(otActualizada.getManoObraMonto());
            ot.setTotalCobrado(otActualizada.getTotalCobrado());
            ot.setEstadoPago(otActualizada.getEstadoPago());
            OrdenTrabajo actualizado = ordenTrabajoService.saveOt(ot);

            if (actualizado.getId() == null) {
                log.warn("No se pudo actualizar la OT: no puede entregarse sin estar pagada");
                return ResponseEntity.badRequest().build();
            }
            log.info("OT {} actualizada correctamente", id);
            return ResponseEntity.ok(actualizado);
        } catch (OrdenTrabajoNotFoundException e) {
            log.error("OT con id {} no encontrada", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> eliminarOt(@PathVariable Long id) {

        log.warn("Solicitud DELETE /api/v1/ot/{}", id);
        try {
            ordenTrabajoService.deletedOt(id);
            log.info("OT {} eliminada correctamente", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar OT con id {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
