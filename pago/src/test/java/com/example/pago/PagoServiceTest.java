package com.example.pago;

import com.example.pago.model.Pago;
import com.example.pago.repository.PagoRepository;
import com.example.pago.service.PagoService;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @InjectMocks
    private PagoService pagoService;

    @Test
    void deberiaRetornarPagoCuandoExiste() {

        Pago pago = new Pago();
        pago.setId(1L);
        pago.setOtId(1L);
        pago.setMonto(50000.0);
        pago.setFormaPago("efectivo");
        pago.setEstado("pagado");

        Mockito.when(pagoRepository.findById(1L))
                .thenReturn(Optional.of(pago));

        Pago resultado = pagoService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals("efectivo", resultado.getFormaPago());
        assertEquals(50000.0, resultado.getMonto());

        verify(pagoRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoPagoNoExiste() {

        Mockito.when(pagoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(Exception.class, () -> pagoService.obtenerPorId(99L));

        verify(pagoRepository).findById(99L);
    }

    @Test
    void deberiaRegistrarPagoCorrectamente() {

        Pago pago = new Pago();
        pago.setOtId(1L);
        pago.setMonto(50000.0);
        pago.setFormaPago("efectivo");

        Mockito.when(pagoRepository.save(pago))
                .thenReturn(pago);

        Pago resultado = pagoService.registrar(pago, "token");

        assertNotNull(resultado);
        assertEquals("pagado", resultado.getEstado());
        assertEquals(50000.0, resultado.getMonto());

        verify(pagoRepository).save(pago);
    }
}
