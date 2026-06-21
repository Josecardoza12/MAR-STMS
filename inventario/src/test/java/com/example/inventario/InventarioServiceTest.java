package com.example.inventario;

import com.example.inventario.model.Repuesto;
import com.example.inventario.repository.InventarioRepository;

import com.example.inventario.service.InventarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class InventarioServiceTest {

    @Mock
    InventarioRepository repository;

    @InjectMocks
    InventarioService service;

    Repuesto repuesto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        repuesto = new Repuesto();
        repuesto.setRepuestoId(1L);
        repuesto.setReparacionId(10L);
        repuesto.setNombre("Carburador Honda GX160");
        repuesto.setProveedor("Proveedor MAR Melipilla");
        repuesto.setCosto(15000.0);
        repuesto.setPrecioSugerido(25000.0);
        repuesto.setStock(5);
    }

    // ============================
    // FIND ALL
    // ============================
    @Test
    void deberiaListarRepuestos() {
        when(repository.findAll()).thenReturn(List.of(repuesto));

        List<Repuesto> lista = service.findAll();

        assertEquals(1, lista.size());
        assertEquals("Carburador Honda GX160", lista.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    // ============================
    // FIND BY ID - OK
    // ============================
    @Test
    void deberiaEncontrarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(repuesto));

        Optional<Repuesto> encontrado = service.findById(1L);

        assertTrue(encontrado.isPresent());
        assertEquals(1L, encontrado.get().getRepuestoId());
        verify(repository, times(1)).findById(1L);
    }

    // ============================
    // FIND BY ID - NOT FOUND
    // ============================
    @Test
    void deberiaRetornarOptionalVacioSiNoExiste() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<Repuesto> resultado = service.findById(1L);

        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).findById(1L);
    }

    // ============================
    // SAVE
    // ============================
    @Test
    void deberiaGuardarRepuesto() {
        when(repository.save(repuesto)).thenReturn(repuesto);

        Repuesto guardado = service.save(repuesto);

        assertNotNull(guardado);
        assertEquals("Carburador Honda GX160", guardado.getNombre());
        verify(repository, times(1)).save(repuesto);
    }

    // ============================
    // DELETE
    // ============================
    @Test
    void deberiaEliminarRepuesto() {
        doNothing().when(repository).deleteById(1L);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
