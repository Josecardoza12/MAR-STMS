package com.example.orden_trabajo;
import com.example.orden_trabajo.exception.OrdenTrabajoNotFoundException;
import com.example.orden_trabajo.model.OrdenTrabajo;
import com.example.orden_trabajo.repository.OrdenTrabajoRepository;
import com.example.orden_trabajo.service.OrdenTrabajoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenTrabajoServiceTest {

    @Mock
    private OrdenTrabajoRepository ordenTrabajoRepository;

    @InjectMocks
    private OrdenTrabajoService ordenTrabajoService;

    @Test
    void deberiaListarOrdenesDeTrabajo() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.findAll())
                .thenReturn(List.of(ot));

        List<OrdenTrabajo> resultado = ordenTrabajoService.listarOt();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1001, resultado.get(0).getNumeroOt());
        assertEquals("INGRESADA", resultado.get(0).getEstado());

        verify(ordenTrabajoRepository).findAll();
    }

    @Test
    void deberiaRetornarOrdenTrabajoCuandoExiste() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("EN_REPARACION");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.findById(1L))
                .thenReturn(Optional.of(ot));

        OrdenTrabajo resultado = ordenTrabajoService.obtenerOtPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1001, resultado.getNumeroOt());
        assertEquals("EN_REPARACION", resultado.getEstado());

        verify(ordenTrabajoRepository).findById(1L);
    }

    @Test
    void deberiaLanzarExcepcionCuandoOrdenTrabajoNoExiste() {
        when(ordenTrabajoRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrdenTrabajoNotFoundException.class,
                () -> ordenTrabajoService.obtenerOtPorId(99L)
        );

        verify(ordenTrabajoRepository).findById(99L);
    }

    @Test
    void deberiaBuscarOrdenesPorCliente() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(5L);
        ot.setIdEquipo(2L);
        ot.setNumeroOt(1002);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.findByIdCliente(5L))
                .thenReturn(List.of(ot));

        List<OrdenTrabajo> resultado = ordenTrabajoService.findByClienteId(5L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(5L, resultado.get(0).getIdCliente());

        verify(ordenTrabajoRepository).findByIdCliente(5L);
    }

    @Test
    void deberiaBuscarOrdenesPorEquipo() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(5L);
        ot.setIdEquipo(3L);
        ot.setNumeroOt(1003);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.findByIdEquipo(3L))
                .thenReturn(List.of(ot));

        List<OrdenTrabajo> resultado = ordenTrabajoService.findByIdEquipo(3L);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(3L, resultado.get(0).getIdEquipo());

        verify(ordenTrabajoRepository).findByIdEquipo(3L);
    }

    @Test
    void deberiaGuardarOrdenTrabajoValida() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        OrdenTrabajo otGuardada = new OrdenTrabajo();
        otGuardada.setId(1L);
        otGuardada.setIdCliente(1L);
        otGuardada.setIdEquipo(1L);
        otGuardada.setNumeroOt(1001);
        otGuardada.setEstado("INGRESADA");
        otGuardada.setFechaIngreso(new Date());
        otGuardada.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.save(ot))
                .thenReturn(otGuardada);

        OrdenTrabajo resultado = ordenTrabajoService.saveOt(ot);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(1001, resultado.getNumeroOt());
        assertEquals("INGRESADA", resultado.getEstado());

        verify(ordenTrabajoRepository).save(ot);
    }

    @Test
    void deberiaAsignarEstadoIngresadaCuandoEstadoVieneVacio() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1004);
        ot.setEstado("");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoRepository.save(any(OrdenTrabajo.class)))
                .thenAnswer(invocation -> {
                    OrdenTrabajo otGuardada = invocation.getArgument(0);
                    otGuardada.setId(1L);
                    return otGuardada;
                });

        OrdenTrabajo resultado = ordenTrabajoService.saveOt(ot);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("INGRESADA", resultado.getEstado());

        verify(ordenTrabajoRepository).save(any(OrdenTrabajo.class));
    }

    @Test
    void noDeberiaGuardarOrdenEntregadaSiNoEstaPagada() {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1005);
        ot.setEstado("ENTREGADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        OrdenTrabajo resultado = ordenTrabajoService.saveOt(ot);

        assertNotNull(resultado);
        assertNull(resultado.getId());
        assertEquals("ENTREGADA", resultado.getEstado());
        assertEquals("PENDIENTE", resultado.getEstadoPago());

        verify(ordenTrabajoRepository, never()).save(any(OrdenTrabajo.class));
    }

    @Test
    void deberiaEliminarOrdenTrabajoPorId() {
        doNothing().when(ordenTrabajoRepository).deleteById(1L);

        String resultado = ordenTrabajoService.deletedOt(1L);

        assertEquals("Orden de trabajo 1 eliminada", resultado);

        verify(ordenTrabajoRepository).deleteById(1L);
    }
}