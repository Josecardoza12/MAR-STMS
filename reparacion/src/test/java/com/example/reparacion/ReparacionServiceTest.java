package com.example.reparacion;

import com.example.reparacion.exception.ReparacionNotFoundException;
import com.example.reparacion.model.Reparacion;
import com.example.reparacion.repository.ReparacionRepository;

import com.example.reparacion.service.ReparacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReparacionServiceTest {

    @Mock
    ReparacionRepository repository;

    @InjectMocks
    ReparacionService service;

    Reparacion reparacion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reparacion = new Reparacion();
        reparacion.setReparacionId(1L);
        reparacion.setOtId(10L);
        reparacion.setFechaInicio(LocalDate.now());
        reparacion.setFechaTermino(LocalDate.now().plusDays(1));
        reparacion.setDetalleTrabajo("Cambio de carburador");
        reparacion.setEstado("EN_REPARACION");
    }

    // ============================
    // FIND ALL
    // ============================
    @Test
    void deberiaListarReparaciones() {
        when(repository.findAll()).thenReturn(List.of(reparacion));

        List<Reparacion> lista = service.findAll();

        assertEquals(1, lista.size());
        assertEquals("Cambio de carburador", lista.get(0).getDetalleTrabajo());
        verify(repository, times(1)).findAll();
    }

    // ============================
    // FIND BY ID - OK
    // ============================
    @Test
    void deberiaEncontrarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(reparacion));

        Reparacion encontrada = service.findById(1L);

        assertNotNull(encontrada);
        assertEquals(1L, encontrada.getReparacionId());
        verify(repository, times(1)).findById(1L);
    }

    // ============================
    // FIND BY ID - NOT FOUND
    // ============================
    @Test
    void deberiaLanzarExcepcionSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ReparacionNotFoundException.class, () -> service.findById(1L));
        verify(repository, times(1)).findById(1L);
    }

    // ============================
    // FIND BY OT ID - OK
    // ============================
    @Test
    void deberiaEncontrarPorOtId() {
        when(repository.findByOtId(10L)).thenReturn(Optional.of(reparacion));

        Reparacion encontrada = service.findByOtId(10L);

        assertNotNull(encontrada);
        assertEquals(10L, encontrada.getOtId());
        verify(repository, times(1)).findByOtId(10L);
    }

    // ============================
    // FIND BY OT ID - NOT FOUND
    // ============================
    @Test
    void deberiaLanzarExcepcionSiNoExistePorOtId() {
        when(repository.findByOtId(10L)).thenReturn(Optional.empty());

        assertThrows(ReparacionNotFoundException.class, () -> service.findByOtId(10L));
        verify(repository, times(1)).findByOtId(10L);
    }

    // ============================
    // SAVE
    // ============================
    @Test
    void deberiaGuardarReparacion() {
        when(repository.save(reparacion)).thenReturn(reparacion);

        Reparacion guardada = service.save(reparacion, "token-falso");

        assertNotNull(guardada);
        assertEquals("Cambio de carburador", guardada.getDetalleTrabajo());
        verify(repository, times(1)).save(reparacion);
    }

    // ============================
    // UPDATE
    // ============================
    @Test
    void deberiaActualizarReparacion() {

        Reparacion nueva = new Reparacion();
        nueva.setOtId(20L);
        nueva.setFechaInicio(LocalDate.now().minusDays(1));
        nueva.setFechaTermino(LocalDate.now());
        nueva.setDetalleTrabajo("Ajuste general");
        nueva.setEstado("TERMINADA");

        when(repository.findById(1L)).thenReturn(Optional.of(reparacion));
        when(repository.save(any(Reparacion.class))).thenReturn(nueva);

        Reparacion actualizada = service.update(1L, nueva, "token-falso");

        assertEquals("Ajuste general", actualizada.getDetalleTrabajo());
        assertEquals("TERMINADA", actualizada.getEstado());
        assertEquals(20L, actualizada.getOtId());

        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Reparacion.class));
    }

    // ============================
    // DELETE
    // ============================
    @Test
    void deberiaEliminarReparacion() {
        when(repository.findById(1L)).thenReturn(Optional.of(reparacion));

        service.delete(1L);

        verify(repository, times(1)).delete(reparacion);
    }
}

