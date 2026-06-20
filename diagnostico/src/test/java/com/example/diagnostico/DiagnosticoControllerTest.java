package com.example.diagnostico;

import com.example.diagnostico.controller.DiagnosticoController;
import com.example.diagnostico.model.Diagnostico;
import com.example.diagnostico.service.DiagnosticoService;
import com.example.diagnostico.client.OrdenTrabajoClient;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DiagnosticoController.class)
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
class DiagnosticoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DiagnosticoService service;

    @MockBean
    OrdenTrabajoClient otClient;

    @Test
    void deberiaListarDiagnosticos() throws Exception {

        Diagnostico d = new Diagnostico();
        d.setDiagnosticoId(1L);
        d.setOtId(10L);
        d.setDescripcion("Equipo no enciende");

        when(service.findAll()).thenReturn(List.of(d));

        mockMvc.perform(get("/api/v1/diagnosticos"))
                .andExpect(status().isOk())
                // HATEOAS moderno: "links" en vez de "_links"
                .andExpect(jsonPath("$[0].links[?(@.rel=='self')].href").exists());
    }

    @Test
    void deberiaBuscarDiagnosticoPorId() throws Exception {

        Diagnostico d = new Diagnostico();
        d.setDiagnosticoId(1L);
        d.setOtId(10L);
        d.setDescripcion("Equipo no enciende");

        when(service.findById(1L)).thenReturn(d);

        mockMvc.perform(get("/api/v1/diagnosticos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }

    @Test
    void deberiaBuscarDiagnosticoPorOtId() throws Exception {

        Diagnostico d = new Diagnostico();
        d.setDiagnosticoId(1L);
        d.setOtId(10L);
        d.setDescripcion("Equipo no enciende");

        when(service.findByOtId(10L)).thenReturn(d);

        mockMvc.perform(get("/api/v1/diagnosticos?otId=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.links[?(@.rel=='self')].href").exists());
    }
}
