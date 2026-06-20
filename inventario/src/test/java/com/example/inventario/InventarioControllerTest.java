package com.example.inventario;

import com.example.inventario.controller.InventarioController;
import com.example.inventario.model.Repuesto;
import com.example.inventario.service.InventarioService;
import com.example.inventario.client.ReparacionClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = InventarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class InventarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    InventarioService service;

    @MockBean
    ReparacionClient reparacionClient;

    // ============================
    // LISTAR TODOS
    // ============================
    @Test
    void deberiaListarRepuestos() throws Exception {

        Repuesto r = new Repuesto();
        r.setRepuestoId(1L);
        r.setReparacionId(10L);
        r.setNombre("Carburador Honda GX160");
        r.setProveedor("Proveedor MAR Melipilla");
        r.setCosto(15000.0);
        r.setPrecioSugerido(25000.0);
        r.setStock(5);

        when(service.findAll()).thenReturn(List.of(r));

        mockMvc.perform(get("/api/v1/inventarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$[0].links[?(@.rel=='eliminar')].href").exists());
    }

    // ============================
    // BUSCAR POR ID
    // ============================
    @Test
    void deberiaBuscarPorId() throws Exception {

        Repuesto r = new Repuesto();
        r.setRepuestoId(1L);
        r.setReparacionId(10L);
        r.setNombre("Carburador Honda GX160");
        r.setProveedor("Proveedor MAR Melipilla");
        r.setCosto(15000.0);
        r.setPrecioSugerido(25000.0);
        r.setStock(5);

        when(service.findById(1L)).thenReturn(java.util.Optional.of(r));

        mockMvc.perform(get("/api/v1/inventarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.links[?(@.rel=='eliminar')].href").exists())
                .andExpect(jsonPath("$.links[?(@.rel=='todos')].href").exists());
    }

    // ============================
    // CREAR
    // ============================
    @Test
    void deberiaCrearRepuesto() throws Exception {

        Repuesto r = new Repuesto();
        r.setRepuestoId(1L);
        r.setReparacionId(10L);
        r.setNombre("Carburador Honda GX160");
        r.setProveedor("Proveedor MAR Melipilla");
        r.setCosto(15000.0);
        r.setPrecioSugerido(25000.0);
        r.setStock(5);

        when(reparacionClient.obtenerReparacion(10L, "token-falso")).thenReturn(null);
        when(service.save(org.mockito.ArgumentMatchers.any(Repuesto.class))).thenReturn(r);

        String json = """
                {
                    "reparacionId": 10,
                    "nombre": "Carburador Honda GX160",
                    "proveedor": "Proveedor MAR Melipilla",
                    "costo": 15000,
                    "precioSugerido": 25000,
                    "stock": 5
                }
                """;

        mockMvc.perform(post("/api/v1/inventarios")
                        .contentType("application/json")
                        .header("Authorization", "token-falso")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.links[?(@.rel=='todos')].href").exists());
    }

    // ============================
    // ACTUALIZAR
    // ============================
    @Test
    void deberiaActualizarRepuesto() throws Exception {

        Repuesto r = new Repuesto();
        r.setRepuestoId(1L);
        r.setReparacionId(10L);
        r.setNombre("Carburador Honda GX160");
        r.setProveedor("Proveedor MAR Melipilla");
        r.setCosto(15000.0);
        r.setPrecioSugerido(25000.0);
        r.setStock(5);

        when(reparacionClient.obtenerReparacion(10L, "token-falso")).thenReturn(null);
        when(service.findById(1L)).thenReturn(java.util.Optional.of(r));
        when(service.save(org.mockito.ArgumentMatchers.any(Repuesto.class))).thenReturn(r);

        String json = """
                {
                    "reparacionId": 10,
                    "nombre": "Carburador Honda GX160",
                    "proveedor": "Proveedor MAR Melipilla",
                    "costo": 15000,
                    "precioSugerido": 25000,
                    "stock": 5
                }
                """;

        mockMvc.perform(put("/api/v1/inventarios/1")
                        .contentType("application/json")
                        .header("Authorization", "token-falso")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists())
                .andExpect(jsonPath("$.links[?(@.rel=='todos')].href").exists())
                .andExpect(jsonPath("$.links[?(@.rel=='eliminar')].href").exists());
    }

    // ============================
    // ELIMINAR
    // ============================
    @Test
    void deberiaEliminarRepuesto() throws Exception {

        mockMvc.perform(delete("/api/v1/inventarios/1"))
                .andExpect(status().isNoContent());
    }
}
