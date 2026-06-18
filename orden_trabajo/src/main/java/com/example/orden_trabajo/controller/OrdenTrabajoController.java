package com.example.orden_trabajo.controller;

import com.example.orden_trabajo.client.EquipoWebClient;
import com.example.orden_trabajo.client.clienteWebClient;
import com.example.orden_trabajo.exception.OrdenTrabajoNotFoundException;
import com.example.orden_trabajo.model.OrdenTrabajo;
import com.example.orden_trabajo.service.OrdenTrabajoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v1/ot")
@SecurityRequirement(name = "bearerAuth")

@Tag(name = "Orden de trabajo" , description = "Gestión de Orden de trabajo registrados en el sistema de MAR-STMS")
public class OrdenTrabajoController {

    @Autowired
    private clienteWebClient clienteWebClient;

    @Autowired
    private EquipoWebClient equipoWebClient;

    @Autowired
    private OrdenTrabajoService ordenTrabajoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Listar órdenes de trabajo",
            description = "Obtiene el listado completo de órdenes de trabajo registradas en el sistema técnico MAR-STMS."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Órdenes de trabajo obtenidas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existen órdenes de trabajo registradas"
            )
    })
    public ResponseEntity<List<OrdenTrabajo>> ListarOt() {
        log.info("Solicitud GET /api/v1/ot");
        List<OrdenTrabajo> ot = ordenTrabajoService.listarOt();
        if (ot.isEmpty()) {
            log.info("No hay orden de trabajo registradas");
            return ResponseEntity.noContent().build();
        }
        for (OrdenTrabajo ordenTrabajo : ot){
            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).obtenerOtPorId(ordenTrabajo.getId())).withSelfRel());
            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).eliminarOt(ordenTrabajo.getId())).withRel("eliminar"));
            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).findByIdCliente(ordenTrabajo.getIdCliente())).withRel("cliente"));

            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).findByIdEquipo(ordenTrabajo.getIdEquipo())).withRel("EQUIPO"));


        }
        log.info("Se encontraron {} orden de trabajo", ot.size());
        return ResponseEntity.ok(ot);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Buscar orden de trabajo por ID",
            description = "Obtiene la información detallada de una orden de trabajo mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden de trabajo encontrada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Orden de trabajo no encontrada"
            )
    })
    public ResponseEntity<OrdenTrabajo> obtenerOtPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/ot/{}", id);
        OrdenTrabajo ot = ordenTrabajoService.obtenerOtPorId(id);
        ot.add(Link.of( "http://localhost:8082/api/v1/clientes/" + ot.getIdCliente(), "cliente"));
        ot.add(Link.of( "http://localhost:8083/api/v1/equipos/" + ot.getIdEquipo(), "equipo"));
        ot.add(linkTo(methodOn(OrdenTrabajoController.class).ListarOt()).withRel("Ordenes de trabajo"));
        ot.add(linkTo(methodOn(OrdenTrabajoController.class).eliminarOt(ot.getId())).withRel("eliminar"));



        log.info("Orden de trabajo encontrada con id {}", id);
        return ResponseEntity.ok(ot);
    }

    @GetMapping(params = "idCliente")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Buscar órdenes de trabajo por cliente",
            description = "Obtiene todas las órdenes de trabajo asociadas a un cliente específico mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Órdenes de trabajo obtenidas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cliente no encontrado"
            )
    })
    public ResponseEntity<List<OrdenTrabajo>> findByIdCliente(@RequestParam Long idCliente) {
        log.info("Solicitud GET /api/v1/ot?idCliente={}", idCliente);
        List<OrdenTrabajo> clienteOt = ordenTrabajoService.findByClienteId(idCliente);
        if (clienteOt.isEmpty()) {
            log.info("No hay orden de trabajo para el cliente {}", idCliente);
            return ResponseEntity.noContent().build();
        }
        for (OrdenTrabajo  ordenTrabajo : clienteOt){
            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).obtenerOtPorId(ordenTrabajo.getId())).withSelfRel());


            ordenTrabajo.add(Link.of( "http://localhost:8082/api/v1/clientes/" + ordenTrabajo.getIdCliente(), "cliente"));

        }
        log.info("Se encontraron {} orden de trabajo para el cliente {}", clienteOt.size(), idCliente);
        return ResponseEntity.ok(clienteOt);
    }

    @GetMapping(params = "idEquipo")
    @PreAuthorize("hasAnyRole('CLIENTE' ,'TECNICO', 'ADMIN')")
    @Operation(
            summary = "Buscar órdenes de trabajo por equipo",
            description = "Obtiene todas las órdenes de trabajo asociadas a un equipo específico mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Órdenes de trabajo obtenidas correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipo no encontrado"
            )
    })
    public ResponseEntity<List<OrdenTrabajo>> findByIdEquipo(@RequestParam Long idEquipo) {
        log.info("Solicitud GET /api/v1/ot?idEquipo={}", idEquipo);
        List<OrdenTrabajo> equipoOt = ordenTrabajoService.findByIdEquipo(idEquipo);
        if (equipoOt.isEmpty()) {
            log.info("No hay orden de trabajo para el equipo {}", idEquipo);
            return ResponseEntity.noContent().build();
        }
        for (OrdenTrabajo  ordenTrabajo : equipoOt){
            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).obtenerOtPorId(ordenTrabajo.getId())).withSelfRel());

            ordenTrabajo.add(linkTo(methodOn(OrdenTrabajoController.class).findByIdCliente(ordenTrabajo.getIdCliente())).withRel(" cliente"));


            ordenTrabajo.add(Link.of( "http://localhost:8083/api/v1/equipos/" + ordenTrabajo.getIdEquipo(), "equipo"));

        }
        log.info("Se encontraron {} orden de trabajo para el equipo {}", equipoOt.size(), idEquipo);
        return ResponseEntity.ok(equipoOt);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('TECNICO', 'ADMIN')")
    @Operation(
            summary = "Registrar orden de trabajo",
            description = "Registra una nueva orden de trabajo validando previamente la existencia del cliente y del equipo asociados."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden de trabajo creada correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    OrdenTrabajo.class)))
    })
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
    @Operation(
            summary = "Actualizar orden de trabajo",
            description = "Actualiza el estado, montos y datos asociados a una orden de trabajo existente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden de trabajo actualizada correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = OrdenTrabajo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Orden de trabajo no encontrada"
            )
    })
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
    @Operation(
            summary = "Eliminar orden de trabajo",
            description = "Elimina una orden de trabajo registrada en el sistema mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Orden de trabajo eliminada correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Orden de trabajo no encontrada"
            )
    })
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
