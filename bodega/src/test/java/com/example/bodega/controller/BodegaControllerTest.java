package com.example.bodega.controller;

import com.example.bodega.model.Bodega;
import com.example.bodega.security.jwt.JwtService;
import com.example.bodega.service.BodegaService;
import com.example.bodega.client.OrdenTrabajoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BodegaController.class)
@AutoConfigureMockMvc(addFilters = false)
class BodegaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BodegaService bodegaService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private OrdenTrabajoClient ordenTrabajoClient;

    @Test
    void deberiaRetornarListaDeBodega() throws Exception {

        Bodega bodega = new Bodega();
        bodega.setId(1L);
        bodega.setOtId(1L);
        bodega.setEstadoCobro("sin_cobro");
        bodega.setMontoBodegaje(0.0);

        when(bodegaService.listarTodos())
                .thenReturn(List.of(bodega));

        mockMvc.perform(get("/api/v1/bodega")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].otId").value(1))
                .andExpect(jsonPath("$[0].estadoCobro").value("sin_cobro"));
    }

    @Test
    void deberiaRetornarBodegaPorOtId() throws Exception {

        Bodega bodega = new Bodega();
        bodega.setId(1L);
        bodega.setOtId(1L);
        bodega.setEstadoCobro("sin_cobro");
        bodega.setMontoBodegaje(0.0);

        when(bodegaService.obtenerPorOtId(1L))
                .thenReturn(Optional.of(bodega));

        mockMvc.perform(get("/api/v1/bodega?otId=1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.otId").value(1))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());
    }
}
