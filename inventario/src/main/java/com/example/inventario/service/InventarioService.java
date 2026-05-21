package com.example.inventario.service;

import com.example.inventario.model.Repuesto;
import com.example.inventario.repository.InventarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class InventarioService {

    private final InventarioRepository repository;

    public InventarioService(InventarioRepository repository) {
        this.repository = repository;
    }

    public List<Repuesto> findAll() {
        log.info("Buscando todos los repuestos");
        List<Repuesto> repuestos = repository.findAll();
        log.info("Se encontraron {} repuestos", repuestos.size());
        return repuestos;
    }

    public Optional<Repuesto> findById(Long id) {
        log.info("Buscando repuesto con id {}", id);
        Optional<Repuesto> repuesto = repository.findById(id);
        if (repuesto.isPresent()) {
            log.info("Repuesto encontrado con id {}", id);
        } else {
            log.warn("No se encontró repuesto con id {}", id);
        }
        return repuesto;
    }

    public Repuesto save(Repuesto repuesto) {
        log.info("Guardando repuesto {}", repuesto.getNombre());
        Repuesto guardado = repository.save(repuesto);
        log.info("Repuesto guardado con id {}", guardado.getRepuestoId());
        return guardado;
    }

    public void delete(Long id) {
        log.warn("Eliminando repuesto con id {}", id);
        repository.deleteById(id);
        log.info("Repuesto con id {} eliminado", id);
    }
}

