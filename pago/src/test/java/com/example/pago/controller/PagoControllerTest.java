package com.example.pago.controller;

import com.example.pago.model.Pago;
import com.example.pago.security.jwt.JwtService;
import com.example.pago.service.PagoService;
import com.example.pago.cliente.OrdenTrabajoClient;
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

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private OrdenTrabajoClient ordenTrabajoClient;

    @Test
    void deberiaRetornarListaDePagos() throws Exception {

        Pago pago = new Pago();
        pago.setId(1L);
        pago.setOtId(1L);
        pago.setMonto(50000.0);
        pago.setFormaPago("efectivo");
        pago.setEstado("pagado");

        when(pagoService.listarTodos())
                .thenReturn(List.of(pago));

        mockMvc.perform(get("/api/v1/pagos")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].monto").value(50000.0))
                .andExpect(jsonPath("$[0].estado").value("pagado"));
    }

    @Test
    void deberiaRetornarPagoPorId() throws Exception {

        Pago pago = new Pago();
        pago.setId(1L);
        pago.setOtId(1L);
        pago.setMonto(50000.0);
        pago.setFormaPago("efectivo");
        pago.setEstado("pagado");

        when(pagoService.obtenerPorId(1L))
                .thenReturn(pago);

        mockMvc.perform(get("/api/v1/pagos/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.monto").value(50000.0))
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.todos.href").exists());
    }
}
