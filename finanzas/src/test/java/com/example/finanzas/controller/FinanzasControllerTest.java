package com.example.finanzas.controller;

import com.example.finanzas.model.Finanzas;
import com.example.finanzas.security.jwt.JwtService;
import com.example.finanzas.service.FinanzasService;
import com.example.finanzas.client.PagoClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FinanzasController.class)
@AutoConfigureMockMvc(addFilters = false)
class FinanzasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinanzasService finanzasService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private PagoClient pagoClient;

    @Test
    void deberiaRetornarListaDeFinanzas() throws Exception {

        Finanzas finanzas = new Finanzas();
        finanzas.setId(1L);
        finanzas.setPagoId(1L);
        finanzas.setCategoria("reparacion");
        finanzas.setTotal(50000.0);

        when(finanzasService.listarTodos())
                .thenReturn(List.of(finanzas));

        mockMvc.perform(get("/api/v1/finanzas")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].categoria").value("reparacion"))
                .andExpect(jsonPath("$[0].total").value(50000.0));
    }

    @Test
    void deberiaRetornarFinanzasPorId() throws Exception {

        Finanzas finanzas = new Finanzas();
        finanzas.setId(1L);
        finanzas.setPagoId(1L);
        finanzas.setCategoria("reparacion");
        finanzas.setTotal(50000.0);

        when(finanzasService.obtenerPorId(1L))
                .thenReturn(finanzas);

        mockMvc.perform(get("/api/v1/finanzas/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.categoria").value("reparacion"))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());
    }
}
