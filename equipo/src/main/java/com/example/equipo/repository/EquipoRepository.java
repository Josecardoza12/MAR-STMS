package com.example.equipo.repository;

import com.example.equipo.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipoRepository extends JpaRepository<Equipo,Long> {
    List<Equipo> findByIdCliente(Long idCliente);
}
