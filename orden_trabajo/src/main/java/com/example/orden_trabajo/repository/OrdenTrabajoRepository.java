package com.example.orden_trabajo.repository;

import com.example.orden_trabajo.model.OrdenTrabajo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenTrabajoRepository extends JpaRepository<OrdenTrabajo,Long> {
    List<OrdenTrabajo> findByIdCliente(Long IdCliente);

    List<OrdenTrabajo> findByIdEquipo(Long IdEquipo);
}
