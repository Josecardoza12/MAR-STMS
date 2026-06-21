package com.example.finanzas;

import com.example.finanzas.model.Finanzas;
import com.example.finanzas.repository.FinanzasRepository;
import com.example.finanzas.service.FinanzasService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FinanzasServiceTest {

    @Mock
    private FinanzasRepository finanzasRepository;

    @InjectMocks
    private FinanzasService finanzasService;

    @Test
    void deberiaRetornarFinanzasCuandoExiste() {

        Finanzas finanzas = new Finanzas();
        finanzas.setId(1L);
        finanzas.setPagoId(1L);
        finanzas.setCategoria("reparacion");
        finanzas.setTotal(50000.0);

        Mockito.when(finanzasRepository.findById(1L))
                .thenReturn(Optional.of(finanzas));

        Finanzas resultado = finanzasService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("reparacion", resultado.getCategoria());
        assertEquals(50000.0, resultado.getTotal());

        verify(finanzasRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoFinanzasNoExiste() {

        Mockito.when(finanzasRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> finanzasService.obtenerPorId(99L));

        verify(finanzasRepository).findById(99L);
    }

    @Test
    void deberiaRegistrarFinanzasCorrectamente() {

        Finanzas finanzas = new Finanzas();
        finanzas.setPagoId(1L);
        finanzas.setCategoria("reparacion");
        finanzas.setTotal(50000.0);

        Mockito.when(finanzasRepository.save(finanzas))
                .thenReturn(finanzas);

        Finanzas resultado = finanzasService.registrar(finanzas);

        assertNotNull(resultado);
        assertEquals("reparacion", resultado.getCategoria());

        verify(finanzasRepository).save(finanzas);
    }
}
