package com.example.orden_trabajo;

import com.example.orden_trabajo.client.EquipoWebClient;
import com.example.orden_trabajo.client.clienteWebClient;
import com.example.orden_trabajo.controller.OrdenTrabajoController;
import com.example.orden_trabajo.model.OrdenTrabajo;
import com.example.orden_trabajo.security.jwt.JwtService;
import com.example.orden_trabajo.service.OrdenTrabajoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrdenTrabajoController.class)
@AutoConfigureMockMvc(addFilters = false)
class OrdenTrabajoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdenTrabajoService ordenTrabajoService;

    @MockBean
    private clienteWebClient clienteClient;

    @MockBean
    private EquipoWebClient equipoClient;

    @MockBean
    private JwtService jwtService;

    @Test
    void deberiaListarOrdenesDeTrabajo() throws Exception {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoService.listarOt())
                .thenReturn(List.of(ot));

        mockMvc.perform(get("/api/v1/ot")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].idEquipo").value(1))
                .andExpect(jsonPath("$[0].numeroOt").value(1001))
                .andExpect(jsonPath("$[0].estado").value("INGRESADA"))
                .andExpect(jsonPath("$[0].estadoPago").value("PENDIENTE"))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.eliminar.href").exists())
                .andExpect(jsonPath("$[0]._links.cliente.href").exists());

        verify(ordenTrabajoService).listarOt();
    }

    @Test
    void deberiaRetornarOrdenTrabajoPorId() throws Exception {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("EN_REPARACION");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoService.obtenerOtPorId(1L))
                .thenReturn(ot);

        mockMvc.perform(get("/api/v1/ot/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.idEquipo").value(1))
                .andExpect(jsonPath("$.numeroOt").value(1001))
                .andExpect(jsonPath("$.estado").value("EN_REPARACION"))
                .andExpect(jsonPath("$.estadoPago").value("PENDIENTE"))
                .andExpect(jsonPath("$._links.cliente.href").exists())
                .andExpect(jsonPath("$._links.equipo.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(ordenTrabajoService).obtenerOtPorId(1L);
    }

    @Test
    void deberiaBuscarOrdenesPorCliente() throws Exception {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(5L);
        ot.setIdEquipo(2L);
        ot.setNumeroOt(1002);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoService.findByClienteId(5L))
                .thenReturn(List.of(ot));

        mockMvc.perform(get("/api/v1/ot?idCliente=5")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(5))
                .andExpect(jsonPath("$[0].idEquipo").value(2))
                .andExpect(jsonPath("$[0].numeroOt").value(1002))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.cliente.href").exists());

        verify(ordenTrabajoService).findByClienteId(5L);
    }

    @Test
    void deberiaBuscarOrdenesPorEquipo() throws Exception {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(5L);
        ot.setIdEquipo(3L);
        ot.setNumeroOt(1003);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");

        when(ordenTrabajoService.findByIdEquipo(3L))
                .thenReturn(List.of(ot));

        mockMvc.perform(get("/api/v1/ot?idEquipo=3")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(5))
                .andExpect(jsonPath("$[0].idEquipo").value(3))
                .andExpect(jsonPath("$[0].numeroOt").value(1003))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.equipo.href").exists());

        verify(ordenTrabajoService).findByIdEquipo(3L);
    }

    @Test
    void deberiaCrearOrdenTrabajo() throws Exception {
        OrdenTrabajo ot = new OrdenTrabajo();
        ot.setId(1L);
        ot.setIdCliente(1L);
        ot.setIdEquipo(1L);
        ot.setNumeroOt(1001);
        ot.setEstado("INGRESADA");
        ot.setFechaIngreso(new Date());
        ot.setEstadoPago("PENDIENTE");
        ot.setDiagnosticoMonto(15000.0);
        ot.setRepuestosMonto(45000.0);
        ot.setManoObraMonto(30000.0);
        ot.setTotalCobrado(90000.0);

        when(clienteClient.obtenerCliente(eq(1L), anyString()))
                .thenReturn(Mono.empty());

        when(equipoClient.obtenerEquipo(eq(1L), anyString()))
                .thenReturn(Mono.empty());

        when(ordenTrabajoService.saveOt(any(OrdenTrabajo.class)))
                .thenReturn(ot);

        String json = """
                {
                    "idCliente": 1,
                    "idEquipo": 1,
                    "numeroOt": 1001,
                    "estado": "INGRESADA",
                    "fechaIngreso": "2026-06-04",
                    "estadoPago": "PENDIENTE",
                    "diagnosticoMonto": 15000.0,
                    "repuestosMonto": 45000.0,
                    "manoObraMonto": 30000.0,
                    "totalCobrado": 90000.0
                }
                """;

        mockMvc.perform(post("/api/v1/ot")
                        .header("Authorization", "Bearer token-test")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.idEquipo").value(1))
                .andExpect(jsonPath("$.numeroOt").value(1001))
                .andExpect(jsonPath("$.estado").value("INGRESADA"))
                .andExpect(jsonPath("$.estadoPago").value("PENDIENTE"));

        verify(clienteClient).obtenerCliente(eq(1L), anyString());
        verify(equipoClient).obtenerEquipo(eq(1L), anyString());
        verify(ordenTrabajoService).saveOt(any(OrdenTrabajo.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        String json = """
                {
                    "idCliente": 1,
                    "idEquipo": 1,
                    "estado": "INGRESADA"
                }
                """;

        mockMvc.perform(post("/api/v1/ot")
                        .header("Authorization", "Bearer token-test")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaEliminarOrdenTrabajo() throws Exception {
        when(ordenTrabajoService.deletedOt(1L))
                .thenReturn("Orden de trabajo 1 eliminada");

        mockMvc.perform(delete("/api/v1/ot/1"))
                .andExpect(status().isNoContent());

        verify(ordenTrabajoService).deletedOt(1L);
    }
}