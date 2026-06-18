package com.example.equipo.controller;

import com.example.equipo.client.ClienteClient;
import com.example.equipo.exception.EquipoNotFoundException;
import com.example.equipo.model.Equipo;
import com.example.equipo.service.EquipoService;
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
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RequestMapping("/api/v1/equipos")
@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Equipos" , description = "Gestión de equipos registrados en el sistema de MAR-STMS")
public class EquipoController {
    @Autowired
    private  ClienteClient clienteClient;
    @Autowired
    private  EquipoService equipoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    @Operation(
            summary = "Obtener equipos",
            description = "Obtiene la lista completa de equipos registrados en el sistema"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de equipos obtenida correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Equipos no encontrados"
            )
    })
    public ResponseEntity<List<Equipo>> listarEquipos(){
        log.info("Solicitud GET /api/v1/equipos");
        List<Equipo> equipo = equipoService.listarEquipos();
        if(equipo.isEmpty()){
            log.info("No hay equipos registrados");
            return ResponseEntity.noContent().build();
        }
        for (Equipo  equipos : equipo){
            equipos.add(linkTo(methodOn(EquipoController.class).obtenerEquipoPorId(equipos.getId())).withSelfRel());
            equipos.add(linkTo(methodOn(EquipoController.class).eliminarEquipo(equipos.getId())).withRel("eliminar"));
            equipos.add(linkTo(methodOn(EquipoController.class).findByIdCliente(equipos.getIdCliente())).withRel("ID del cliente"));


        }
        log.info("Se encontraron {} equipos", equipo.size());
        return ResponseEntity.ok(equipo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    @Operation(
            summary = "Buscar equipo por ID",
            description = "Obtiene la información detallada de un equipo registrado mediante su identificador único."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipo encontrado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró un equipo con el ID proporcionado"
            )
    })
    public ResponseEntity<Equipo> obtenerEquipoPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/equipos/{}", id);
        Equipo equipo = equipoService.obtenerPorId(id);
        equipo.add(Link.of( "http://localhost:8082/api/v1/clientes/" + equipo.getIdCliente(), "cliente"));
        equipo.add(linkTo(methodOn(EquipoController.class).listarEquipos()).withRel("Equipos"));
        equipo.add(linkTo(methodOn(EquipoController.class).eliminarEquipo(id)).withRel("Eliminar Equipos"));

        log.info("Equipo encontrado con id {}", id);
        return ResponseEntity.ok(equipo);
    }

    @GetMapping(params = "modelo")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public List<Equipo> findByModeloContaining(@RequestParam String modelo){
        return equipoService.findByModeloContaining(modelo);
    }


    @GetMapping(params = "idCliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    @Operation(
            summary = "Buscar equipos por cliente",
            description = "Obtiene todos los equipos asociados a un cliente registrado mediante su ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipos del cliente obtenidos correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "El cliente no posee equipos registrados"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró un cliente con el ID proporcionado"
            )
    })

    public ResponseEntity<List<Equipo>> findByIdCliente(@RequestParam Long idCliente) {
        log.info("Solicitud GET /api/v1/equipos?idCliente={}", idCliente);
        List<Equipo> equipos = equipoService.findByClienteId(idCliente);
        if (equipos.isEmpty()) {
            log.info("No hay equipos para el cliente {}", idCliente);
            return ResponseEntity.noContent().build();
        }
        for (Equipo  equipo : equipos){
            equipo.add(linkTo(methodOn(EquipoController.class).obtenerEquipoPorId(equipo.getId())).withSelfRel());


            equipo.add(Link.of( "http://localhost:8082/api/v1/clientes/" + equipo.getIdCliente(), "cliente"));

        }
        log.info("Se encontraron {} equipos para el cliente {}", equipos.size(), idCliente);
        return ResponseEntity.ok(equipos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @Operation(
            summary = "Registrar un  equipo",
            description = "Crea un nuevo equipo asociado a un cliente dentro del sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation =
                                    Equipo.class)))
    })
    public ResponseEntity<Equipo> crearEquipo(@Valid @RequestBody Equipo equipo, @RequestHeader("Authorization") String token) {
        log.info("Solicitud POST /api/v1/equipos - creando equipo para cliente {}", equipo.getIdCliente());
        clienteClient.obtenerCliente(equipo.getIdCliente(), token).block();
        log.info("Cliente {} validado correctamente", equipo.getIdCliente());
        Equipo guardado = equipoService.saveEquipo(equipo);
        if(guardado.getId() == null){
            log.warn("No se pudo crear el equipo: datos incompletos");
            return ResponseEntity.badRequest().build();
        }
        guardado.add(linkTo(methodOn(EquipoController.class).listarEquipos()).withRel
                ("todos"));
        guardado.add(linkTo(methodOn(EquipoController.class).obtenerEquipoPorId(guardado.getId()
                )).withSelfRel());
        guardado.add(linkTo(methodOn(EquipoController.class).eliminarEquipo(guardado.getId())).withRel("eliminar"));
        log.info("Equipo creado correctamente con id {}", guardado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    @Operation(
            summary = "Actualizar equipo",
            description = "Actualiza la información de un equipo registrado en el sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Equipo actualizado correctamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Equipo.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el equipo "
            )
    })
    public ResponseEntity<Equipo> actualizarEquipo(@PathVariable Long id, @Valid @RequestBody Equipo equipoActualizado,
                                             @RequestHeader("Authorization") String token) {
        log.info("Solicitud PUT /api/v1/equipos/{}", id);
        try {
            clienteClient.obtenerCliente(equipoActualizado.getIdCliente(), token).block();


            Equipo equipo = equipoService.obtenerPorId(id);

            equipo.setTipoEquipo(equipoActualizado.getTipoEquipo());
            equipo.setModelo(equipoActualizado.getModelo());
            equipo.setMarca(equipoActualizado.getMarca());
            equipo.setNumeroSerie(equipoActualizado.getNumeroSerie());
            equipo.setEstadoIngreso(equipoActualizado.getEstadoIngreso());

            Equipo actualizado = equipoService.saveEquipo(equipo);

            if(actualizado.getId() == null){
                log.warn("No se pudo actualizar el equipo: datos inválidos");
                return ResponseEntity.badRequest().build();
            }
            log.info("Equipo {} actualizado correctamente", id);
            return ResponseEntity.ok(actualizado);
        } catch (EquipoNotFoundException e) {
            log.error("Equipo con id {} no encontrado", id);
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Eliminar equipo",
            description = "Elimina un equipo registrado del sistema mediante su identificador."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Equipo eliminado correctamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontró el equipo que se desea eliminar"
            )
    })
    public ResponseEntity<?> eliminarEquipo(@PathVariable Long id){
        log.warn("Solicitud DELETE /api/v1/equipos/{}", id);
        try{
            equipoService.deletedEquipo(id);
            log.info("Equipo {} eliminado correctamente", id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            log.error("Error al eliminar equipo con id {}", id);
            return ResponseEntity.notFound().build();
        }
    }



}

