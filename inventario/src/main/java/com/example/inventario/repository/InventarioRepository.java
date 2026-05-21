package com.example.inventario.repository;

import com.example.inventario.model.Repuesto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventarioRepository extends JpaRepository< Repuesto, Long> {
}

