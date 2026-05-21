package com.example.diagnostico.service;

import com.example.diagnostico.client.OrdenTrabajoClient;
import com.example.diagnostico.exception.DiagnosticoNotFoundException;
import com.example.diagnostico.model.Diagnostico;
import com.example.diagnostico.repository.DiagnosticoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiagnosticoService {

    private final DiagnosticoRepository repository;
    private final OrdenTrabajoClient otClient;

    public List<Diagnostico> findAll() {
        log.info("Listando todos los diagnósticos");
        return repository.findAll();
    }

    public Diagnostico findById(Long id) {
        log.info("Buscando diagnóstico {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new DiagnosticoNotFoundException(id));
    }

    public Diagnostico findByOtId(Long otId) {
        log.info("Buscando diagnóstico por OT {}", otId);
        return repository.findByOtId(otId)
                .orElseThrow(() -> new DiagnosticoNotFoundException(otId));
    }

    public Diagnostico save(Diagnostico diagnostico, String token) {

        log.info("Validando existencia de OT {} en orden_trabajo", diagnostico.getOtId());



        log.info("Guardando diagnóstico {}", diagnostico);
        return repository.save(diagnostico);
    }

    public Diagnostico update(Long id, Diagnostico diagnostico, String token) {

        log.info("Actualizando diagnóstico {}", id);

        Diagnostico existing = findById(id);


        existing.setDescripcion(diagnostico.getDescripcion());
        existing.setCausaProbable(diagnostico.getCausaProbable());
        existing.setHallazgo(diagnostico.getHallazgo());
        existing.setEstado(diagnostico.getEstado());
        existing.setFechaDiagnostico(diagnostico.getFechaDiagnostico());
        existing.setOtId(diagnostico.getOtId());

        return repository.save(existing);
    }

    public void delete(Long id) {
        log.warn("Eliminando diagnóstico {}", id);
        repository.delete(findById(id));
    }
}
