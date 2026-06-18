package com.example.equipo;

import com.example.equipo.client.ClienteClient;
import com.example.equipo.controller.EquipoController;
import com.example.equipo.model.Cliente;
import com.example.equipo.model.Equipo;
import com.example.equipo.security.jwt.JwtService;
import com.example.equipo.service.EquipoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EquipoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EquipoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EquipoService equipoService;

    @MockBean
    private ClienteClient clienteClient;

    @MockBean
    private JwtService jwtService;

    @Test
    void deberiaListarEquipos() throws Exception {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Generador eléctrico");
        equipo.setMarca("Honda");
        equipo.setModelo("EU2200i");
        equipo.setNumeroSerie("GEN-12345");
        equipo.setEstadoIngreso("No enciende");

        when(equipoService.listarEquipos())
                .thenReturn(List.of(equipo));

        mockMvc.perform(get("/api/v1/equipos")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].tipoEquipo").value("Generador eléctrico"))
                .andExpect(jsonPath("$[0].marca").value("Honda"))
                .andExpect(jsonPath("$[0].modelo").value("EU2200i"))
                .andExpect(jsonPath("$[0].numeroSerie").value("GEN-12345"))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.eliminar.href").exists());

        verify(equipoService).listarEquipos();
    }

    @Test
    void deberiaRetornarEquipoPorId() throws Exception {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Motosierra");
        equipo.setMarca("Stihl");
        equipo.setModelo("MS250");
        equipo.setNumeroSerie("STH-98765");
        equipo.setEstadoIngreso("Cadena suelta");

        when(equipoService.obtenerPorId(1L))
                .thenReturn(equipo);

        mockMvc.perform(get("/api/v1/equipos/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.tipoEquipo").value("Motosierra"))
                .andExpect(jsonPath("$.marca").value("Stihl"))
                .andExpect(jsonPath("$.modelo").value("MS250"))
                .andExpect(jsonPath("$.numeroSerie").value("STH-98765"))
                .andExpect(jsonPath("$._links.cliente.href").exists())
                .andExpect(jsonPath("$._links.Equipos.href").exists());

        verify(equipoService).obtenerPorId(1L);
    }

    @Test
    void deberiaBuscarEquiposPorCliente() throws Exception {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(5L);
        equipo.setTipoEquipo("Cortadora de pasto");
        equipo.setMarca("Husqvarna");
        equipo.setModelo("LC140");
        equipo.setNumeroSerie("HUS-555");
        equipo.setEstadoIngreso("Motor no parte");

        when(equipoService.findByClienteId(5L))
                .thenReturn(List.of(equipo));

        mockMvc.perform(get("/api/v1/equipos?idCliente=5")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].idCliente").value(5))
                .andExpect(jsonPath("$[0].marca").value("Husqvarna"))
                .andExpect(jsonPath("$[0].modelo").value("LC140"))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.cliente.href").exists());

        verify(equipoService).findByClienteId(5L);
    }

    @Test
    void deberiaBuscarEquiposPorModelo() throws Exception {
        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Motosierra");
        equipo.setMarca("Stihl");
        equipo.setModelo("MS250");
        equipo.setNumeroSerie("STH-98765");

        when(equipoService.findByModeloContaining("MS"))
                .thenReturn(List.of(equipo));

        mockMvc.perform(get("/api/v1/equipos?modelo=MS")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].modelo").value("MS250"))
                .andExpect(jsonPath("$[0].marca").value("Stihl"));

        verify(equipoService).findByModeloContaining("MS");
    }

    @Test
    void deberiaCrearEquipo() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");

        Equipo equipo = new Equipo();
        equipo.setId(1L);
        equipo.setIdCliente(1L);
        equipo.setTipoEquipo("Generador eléctrico");
        equipo.setMarca("Honda");
        equipo.setModelo("EU2200i");
        equipo.setNumeroSerie("GEN-12345");
        equipo.setEstadoIngreso("No enciende");

        when(clienteClient.obtenerCliente(eq(1L), any(String.class)))
                .thenReturn(Mono.just(cliente));

        when(equipoService.saveEquipo(any(Equipo.class)))
                .thenReturn(equipo);

        String json = """
                {
                    "idCliente": 1,
                    "tipoEquipo": "Generador eléctrico",
                    "marca": "Honda",
                    "modelo": "EU2200i",
                    "numeroSerie": "GEN-12345",
                    "estadoIngreso": "No enciende"
                }
                """;

        mockMvc.perform(post("/api/v1/equipos")
                        .header("Authorization", "Bearer token-test")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.tipoEquipo").value("Generador eléctrico"))
                .andExpect(jsonPath("$.marca").value("Honda"))
                .andExpect(jsonPath("$.modelo").value("EU2200i"))
                .andExpect(jsonPath("$.numeroSerie").value("GEN-12345"))
                .andExpect(jsonPath("$._links.todos.href").exists())
                .andExpect(jsonPath("$._links.self.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(clienteClient).obtenerCliente(eq(1L), any(String.class));
        verify(equipoService).saveEquipo(any(Equipo.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        String json = """
                {
                    "idCliente": 1,
                    "tipoEquipo": "Generador eléctrico"
                }
                """;

        mockMvc.perform(post("/api/v1/equipos")
                        .header("Authorization", "Bearer token-test")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaEliminarEquipo() throws Exception {
        when(equipoService.deletedEquipo(1L))
                .thenReturn("Equipo 1 eliminado");

        mockMvc.perform(delete("/api/v1/equipos/1"))
                .andExpect(status().isNoContent());

        verify(equipoService).deletedEquipo(1L);
    }
}