package com.example.bodega.service;

import com.example.bodega.exception.BodegaNotFoundException;
import com.example.bodega.model.Bodega;
import com.example.bodega.repository.BodegaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BodegaService {

    @Autowired
    private BodegaRepository bodegaRepository;

    private static final int DIAS_GRATIS = 15;
    private static final double MONTO_DIARIO = 1000.0;

    public List<Bodega> listarTodos() {
        log.info("Listando todos los registros de bodega");
        return bodegaRepository.findAll();
    }

    public Bodega obtenerPorId(Long id) {
        log.info("Buscando bodega con id {}", id);
        return bodegaRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Bodega con id {} no encontrada", id);
                    return new BodegaNotFoundException(id);
                });
    }

    public Optional<Bodega> obtenerPorOtId(Long otId) {
        log.info("Buscando bodega para OT {}", otId);
        return bodegaRepository.findByOtId(otId);
    }

    public Bodega registrar(Bodega bodega) {
        log.info("Registrando equipo en bodega para OT {}", bodega.getOtId());
        bodega.setFechaListo(LocalDate.now());
        bodega.setDiasEnBodega(0);
        bodega.setEstadoCobro("sin_cobro");
        bodega.setMontoBodegaje(0.0);
        Bodega saved = bodegaRepository.save(bodega);
        log.info("Equipo registrado en bodega con id {}", saved.getId());
        return saved;
    }

    public Bodega actualizar(Long id) {
        log.info("Actualizando bodega con id {}", id);
        Bodega bodega = obtenerPorId(id);
        long dias = ChronoUnit.DAYS.between(bodega.getFechaListo(), LocalDate.now());
        bodega.setDiasEnBodega((int) dias);
        if (dias > DIAS_GRATIS) {
            bodega.setEstadoCobro("con_cobro");
            long diasCobrados = dias - DIAS_GRATIS;
            bodega.setMontoBodegaje(diasCobrados * MONTO_DIARIO);
            log.warn("Equipo en bodega {} lleva {} días - Monto: ${}", id, dias, bodega.getMontoBodegaje());
        } else {
            log.info("Equipo en bodega {} lleva {} días - Sin cobro", id, dias);
        }
        return bodegaRepository.save(bodega);
    }

    public String eliminar(Long id) {
        log.info("Eliminando registro de bodega con id {}", id);
        obtenerPorId(id);
        bodegaRepository.deleteById(id);
        log.info("Registro de bodega {} eliminado correctamente", id);
        return "Bodega " + id + " eliminada";
    }
}