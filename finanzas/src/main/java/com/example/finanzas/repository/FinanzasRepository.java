package com.example.finanzas.repository;

import com.example.finanzas.model.Finanzas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinanzasRepository extends JpaRepository<Finanzas, Long> {


    List<Finanzas> findByPagoId(Long PagoId);

}
