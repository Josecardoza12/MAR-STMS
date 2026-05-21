package com.example.equipo.controller;

import com.example.equipo.client.ClienteClient;
import com.example.equipo.exception.EquipoNotFoundException;
import com.example.equipo.model.Equipo;
import com.example.equipo.service.EquipoService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/v1/equipos")

public class EquipoController {
    @Autowired
    private  ClienteClient clienteClient;
    @Autowired
    private  EquipoService equipoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<List<Equipo>> listarEquipos(){
        log.info("Solicitud GET /api/v1/equipos");
        List<Equipo> equipo = equipoService.listarEquipos();
        if(equipo.isEmpty()){
            log.info("No hay equipos registrados");
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} equipos", equipo.size());
        return ResponseEntity.ok(equipo);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<Equipo> obtenerEquipoPorId(@PathVariable Long id) {
        log.info("Solicitud GET /api/v1/equipos/{}", id);
        Equipo equipo = equipoService.obtenerPorId(id);
        log.info("Equipo encontrado con id {}", id);
        return ResponseEntity.ok(equipo);
    }


    @GetMapping(params = "idCliente")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO', 'CLIENTE')")
    public ResponseEntity<List<Equipo>> findByIdCliente(@RequestParam Long idCliente) {
        log.info("Solicitud GET /api/v1/equipos?idCliente={}", idCliente);
        List<Equipo> equipos = equipoService.findByClienteId(idCliente);
        if (equipos.isEmpty()) {
            log.info("No hay equipos para el cliente {}", idCliente);
            return ResponseEntity.noContent().build();
        }
        log.info("Se encontraron {} equipos para el cliente {}", equipos.size(), idCliente);
        return ResponseEntity.ok(equipos);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
    public ResponseEntity<Equipo> crearEquipo(@Valid @RequestBody Equipo equipo, @RequestHeader("Authorization") String token) {
        log.info("Solicitud POST /api/v1/equipos - creando equipo para cliente {}", equipo.getIdCliente());
        clienteClient.obtenerCliente(equipo.getIdCliente(), token).block();
        log.info("Cliente {} validado correctamente", equipo.getIdCliente());
        Equipo guardado = equipoService.saveEquipo(equipo);
        if(guardado.getId() == null){
            log.warn("No se pudo crear el equipo: datos incompletos");
            return ResponseEntity.badRequest().build();
        }
        log.info("Equipo creado correctamente con id {}", guardado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECNICO')")
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

