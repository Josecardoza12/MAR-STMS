package com.example.bodega;

import com.example.bodega.model.Bodega;
import com.example.bodega.repository.BodegaRepository;
import com.example.bodega.service.BodegaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BodegaServiceTest {

    @Mock
    private BodegaRepository bodegaRepository;

    @InjectMocks
    private BodegaService bodegaService;

    @Test
    void deberiaRetornarBodegaCuandoExiste() {

        Bodega bodega = new Bodega();
        bodega.setId(1L);
        bodega.setOtId(1L);
        bodega.setEstadoCobro("sin_cobro");
        bodega.setMontoBodegaje(0.0);

        Mockito.when(bodegaRepository.findById(1L))
                .thenReturn(Optional.of(bodega));

        Bodega resultado = bodegaService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("sin_cobro", resultado.getEstadoCobro());
        assertEquals(0.0, resultado.getMontoBodegaje());

        verify(bodegaRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoBodegaNoExiste() {

        Mockito.when(bodegaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> bodegaService.obtenerPorId(99L));

        verify(bodegaRepository).findById(99L);
    }

    @Test
    void deberiaRegistrarBodegaCorrectamente() {

        Bodega bodega = new Bodega();
        bodega.setOtId(1L);

        Mockito.when(bodegaRepository.save(bodega))
                .thenReturn(bodega);

        Bodega resultado = bodegaService.registrar(bodega);

        assertNotNull(resultado);
        assertEquals("sin_cobro", resultado.getEstadoCobro());
        assertEquals(0.0, resultado.getMontoBodegaje());
        assertEquals(0, resultado.getDiasEnBodega());
        assertEquals(LocalDate.now(), resultado.getFechaListo());

        verify(bodegaRepository).save(bodega);
    }
}
