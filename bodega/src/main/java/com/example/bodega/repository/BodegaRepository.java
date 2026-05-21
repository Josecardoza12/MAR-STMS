package com.example.bodega.repository;

import com.example.bodega.model.Bodega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BodegaRepository extends JpaRepository<Bodega, Long> {

    Optional<Bodega> findByOtId(Long otId);

}
