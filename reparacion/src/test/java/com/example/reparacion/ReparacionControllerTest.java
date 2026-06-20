package com.example.reparacion;

import com.example.reparacion.controller.ReparacionController;
import com.example.reparacion.model.Reparacion;
import com.example.reparacion.service.ReparacionService;
import com.example.reparacion.client.OrdenTrabajoClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReparacionController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class ReparacionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReparacionService service;

    @MockBean
    OrdenTrabajoClient ordenTrabajoClient; // ← ESTE ERA EL QUE FALTABA

    // ============================
    // LISTAR TODAS
    // ============================
    @Test
    void deberiaListarReparaciones() throws Exception {

        Reparacion r = new Reparacion();
        r.setReparacionId(1L);
        r.setOtId(10L);
        r.setFechaInicio(LocalDate.now());
        r.setDetalleTrabajo("Cambio de carburador");
        r.setEstado("EN_REPARACION");

        when(service.findAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/reparaciones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].links[?(@.rel=='self')].href").exists());
    }

    // ============================
    // BUSCAR POR ID
    // ============================
    @Test
    void deberiaBuscarPorId() throws Exception {

        Reparacion r = new Reparacion();
        r.setReparacionId(1L);
        r.setOtId(10L);
        r.setDetalleTrabajo("Cambio de carburador");
        r.setEstado("EN_REPARACION");

        when(service.findById(1L)).thenReturn(r);

        mockMvc.perform(get("/api/v1/reparaciones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }

    // ============================
    // BUSCAR POR OT ID
    // ============================
    @Test
    void deberiaBuscarPorOtId() throws Exception {

        Reparacion r = new Reparacion();
        r.setReparacionId(1L);
        r.setOtId(10L);
        r.setDetalleTrabajo("Cambio de carburador");
        r.setEstado("EN_REPARACION");

        when(service.findByOtId(10L)).thenReturn(r);

        mockMvc.perform(get("/api/v1/reparaciones?otId=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }

    // ============================
    // CREAR
    // ============================
    @Test
    void deberiaCrearReparacion() throws Exception {

        Reparacion r = new Reparacion();
        r.setReparacionId(1L);
        r.setOtId(10L);
        r.setDetalleTrabajo("Cambio de carburador");
        r.setEstado("EN_REPARACION");

        when(ordenTrabajoClient.obtenerOt(10L, "token-falso")).thenReturn(null);
        when(service.save(org.mockito.ArgumentMatchers.any(Reparacion.class), org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(r);

        String json = """
                {
                    "otId": 10,
                    "detalleTrabajo": "Cambio de carburador",
                    "estado": "EN_REPARACION"
                }
                """;

        mockMvc.perform(post("/api/v1/reparaciones")
                        .contentType("application/json")
                        .header("Authorization", "token-falso")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }

    // ============================
    // ACTUALIZAR
    // ============================
    @Test
    void deberiaActualizarReparacion() throws Exception {

        Reparacion r = new Reparacion();
        r.setReparacionId(1L);
        r.setOtId(10L);
        r.setDetalleTrabajo("Cambio de carburador");
        r.setEstado("EN_REPARACION");

        when(ordenTrabajoClient.obtenerOt(10L, "token-falso")).thenReturn(null);
        when(service.update(
                org.mockito.ArgumentMatchers.eq(1L),
                org.mockito.ArgumentMatchers.any(Reparacion.class),
                org.mockito.ArgumentMatchers.anyString()
        )).thenReturn(r);

        String json = """
                {
                    "detalleTrabajo": "Cambio de carburador",
                    "estado": "EN_REPARACION"
                }
                """;

        mockMvc.perform(put("/api/v1/reparaciones/1")
                        .contentType("application/json")
                        .header("Authorization", "token-falso")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }

    // ============================
    // ELIMINAR
    // ============================
    @Test
    void deberiaEliminarReparacion() throws Exception {

        mockMvc.perform(delete("/api/v1/reparaciones/1"))
                .andExpect(status().isNoContent());
    }
}
