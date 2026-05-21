package com.example.reparacion.repository;

import com.example.reparacion.model.Reparacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReparacionRepository extends JpaRepository<Reparacion, Long> {
    Optional<Reparacion> findByOtId(Long id);
}
