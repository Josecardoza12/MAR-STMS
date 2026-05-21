package com.example.diagnostico.repository;

import com.example.diagnostico.model.Diagnostico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiagnosticoRepository extends JpaRepository<Diagnostico, Long> {

    Optional<Diagnostico> findByOtId(Long otId);
}
