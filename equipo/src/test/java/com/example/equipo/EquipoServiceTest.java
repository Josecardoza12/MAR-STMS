package com.example.equipo;


import com.example.equipo.exception.EquipoNotFoundException;
import com.example.equipo.model.Equipo;
import com.example.equipo.repository.EquipoRepository;
import com.example.equipo.service.EquipoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EquipoServiceTest {

    @Mock
    private EquipoRepository equipoRepository;

    @InjectMocks
    private EquipoService equipoService;

    @Test
    void deberiaListarEquipos() {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Generador eléctrico");
        equipo.setMarca("Honda");
        equipo.setModelo("EU2200i");
        equipo.setNumeroSerie("GEN-12345");
        equipo.setEstadoIngreso("No enciende");

        when(equipoRepository.findAll())
                .thenReturn(List.of(equipo));

        List<Equipo> resultado = equipoService.listarEquipos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Honda", resultado.get(0).getMarca());
        assertEquals("EU2200i", resultado.get(0).getModelo());

        verify(equipoRepository).findAll();
    }

    @Test
    void deberiaRetornarEquipoCuandoExiste() {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Motosierra");
        equipo.setMarca("Stihl");
        equipo.setModelo("MS250");
        equipo.setNumeroSerie("STH-98765");
        equipo.setEstadoIngreso("Cadena suelta");

        when(equipoRepository.findById(1L))
                .thenReturn(Optional.of(equipo));

        Equipo resultado = equipoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Stihl", resultado.getMarca());
        assertEquals("MS250", resultado.getModelo());

        verify(equipoRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoEquipoNoExiste() {
        when(equipoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                EquipoNotFoundException.class,
                () -> equipoService.obtenerPorId(99L)
        );

        verify(equipoRepository).findById(99L);
    }

    @Test
    void deberiaBuscarEquiposPorCliente() {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(5L);
        equipo.setTipoEquipo("Cortadora de pasto");
        equipo.setMarca("Husqvarna");
        equipo.setModelo("LC140");
        equipo.setNumeroSerie("HUS-555");

        when(equipoRepository.findByIdCliente(5L))
                .thenReturn(List.of(equipo));

        List<Equipo> resultado = equipoService.findByClienteId(5L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getIdCliente());

        verify(equipoRepository).findByIdCliente(5L);
    }


    @Test
    void deberiaGuardarEquipoValido() {
        Equipo equipo = new Equipo();
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Generador eléctrico");
        equipo.setMarca("Honda");
        equipo.setModelo("EU2200i");
        equipo.setNumeroSerie("GEN-12345");
        equipo.setEstadoIngreso("No enciende");

        Equipo equipoGuardado = new Equipo();
        equipoGuardado.setId(1L);
        equipoGuardado.setIdCliente(1L);
        equipoGuardado.setTipoEquipo("Generador eléctrico");
        equipoGuardado.setMarca("Honda");
        equipoGuardado.setModelo("EU2200i");
        equipoGuardado.setNumeroSerie("GEN-12345");
        equipoGuardado.setEstadoIngreso("No enciende");

        when(equipoRepository.save(equipo))
                .thenReturn(equipoGuardado);

        Equipo resultado = equipoService.saveEquipo(equipo);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Honda", resultado.getMarca());
        assertEquals("EU2200i", resultado.getModelo());

        verify(equipoRepository).save(equipo);
    }

    @Test
    void noDeberiaGuardarEquipoSinNumeroSerie() {
        Equipo equipo = new Equipo();
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Motosierra");
        equipo.setMarca("Stihl");
        equipo.setModelo("MS250");
        equipo.setNumeroSerie("");

        Equipo resultado = equipoService.saveEquipo(equipo);

        assertNotNull(resultado);
        assertNull(resultado.getId());

        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void noDeberiaGuardarEquipoSinMarca() {
        Equipo equipo = new Equipo();
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Motosierra");
        equipo.setMarca("");
        equipo.setModelo("MS250");
        equipo.setNumeroSerie("STH-12345");

        Equipo resultado = equipoService.saveEquipo(equipo);

        assertNotNull(resultado);
        assertNull(resultado.getId());

        verify(equipoRepository, never()).save(any(Equipo.class));
    }

    @Test
    void deberiaEliminarEquipoPorId() {
        doNothing().when(equipoRepository).deleteById(1L);

        String resultado = equipoService.deletedEquipo(1L);

        assertEquals("Equipo 1 eliminado", resultado);

        verify(equipoRepository).deleteById(1L);
    }
}