package com.example.cliente;

import com.example.cliente.controller.ClienteController;
import com.example.cliente.model.Cliente;
import com.example.cliente.security.jwt.JwtService;
import com.example.cliente.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private JwtService jwtService;

    @Test
    void deberiaListarClientes() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");
        cliente.setTelefono("+56912345678");
        cliente.setDireccion("Melipilla");
        cliente.setTipo_Cliente("Frecuente");

        when(clienteService.listarClientes())
                .thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/v1/clientes")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Jose Cardoza"))
                .andExpect(jsonPath("$[0].rut").value("12345678-9"))
                .andExpect(jsonPath("$[0]._links.self.href").exists())
                .andExpect(jsonPath("$[0]._links.eliminar.href").exists());

        verify(clienteService).listarClientes();
    }

    @Test
    void deberiaRetornarClientePorId() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");
        cliente.setTelefono("+56912345678");
        cliente.setDireccion("Melipilla");
        cliente.setTipo_Cliente("Frecuente");

        when(clienteService.obtenerPorId(1L))
                .thenReturn(cliente);

        mockMvc.perform(get("/api/v1/clientes/1")
                        .accept(MediaTypes.HAL_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Jose Cardoza"))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$._links.Clientes.href").exists())
                .andExpect(jsonPath("$._links.eliminar.href").exists());

        verify(clienteService).obtenerPorId(1L);
    }

    @Test
    void deberiaCrearCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");
        cliente.setTelefono("+56912345678");
        cliente.setDireccion("Melipilla");
        cliente.setTipo_Cliente("Frecuente");

        when(clienteService.saveCliente(any(Cliente.class)))
                .thenReturn(cliente);

        String json = """
                {
                    "nombre": "Jose Cardoza",
                    "rut": "12345678-9",
                    "telefono": "+56912345678",
                    "direccion": "Melipilla",
                    "tipo_Cliente": "Frecuente"
                }
                """;

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType("application/json")
                        .accept(MediaTypes.HAL_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Jose Cardoza"))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.telefono").value("+56912345678"))
                .andExpect(jsonPath("$.direccion").value("Melipilla"))
                .andExpect(jsonPath("$.tipo_Cliente").value("Frecuente"));

        verify(clienteService).saveCliente(any(Cliente.class));
    }

    @Test
    void deberiaActualizarCliente() throws Exception {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setId(1L);
        clienteExistente.setNombre("Jose Antiguo");
        clienteExistente.setRut("12345678-9");
        clienteExistente.setTelefono("+56911111111");
        clienteExistente.setDireccion("Melipilla");
        clienteExistente.setTipo_Cliente("Normal");

        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setId(1L);
        clienteActualizado.setNombre("Jose Cardoza");
        clienteActualizado.setRut("12345678-9");
        clienteActualizado.setTelefono("+56912345678");
        clienteActualizado.setDireccion("Melipilla Centro");
        clienteActualizado.setTipo_Cliente("Frecuente");

        when(clienteService.obtenerPorId(1L))
                .thenReturn(clienteExistente);

        when(clienteService.saveCliente(any(Cliente.class)))
                .thenReturn(clienteActualizado);

        String json = """
            {
                "nombre": "Jose Cardoza",
                "rut": "12345678-9",
                "telefono": "+56912345678",
                "direccion": "Melipilla Centro",
                "tipo_Cliente": "Frecuente"
            }
            """;

        mockMvc.perform(put("/api/v1/clientes/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Jose Cardoza"))
                .andExpect(jsonPath("$.rut").value("12345678-9"))
                .andExpect(jsonPath("$.telefono").value("+56912345678"))
                .andExpect(jsonPath("$.direccion").value("Melipilla Centro"))
                .andExpect(jsonPath("$.tipo_Cliente").value("Frecuente"));

        verify(clienteService).obtenerPorId(1L);
        verify(clienteService).saveCliente(any(Cliente.class));
    }

    @Test
    void deberiaRetornar400CuandoFaltanCamposObligatorios() throws Exception {
        String json = """
                {
                    "telefono": "+56912345678",
                    "direccion": "Melipilla",
                    "tipo_Cliente": "Frecuente"
                }
                """;

        mockMvc.perform(post("/api/v1/clientes")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberiaEliminarCliente() throws Exception {
        when(clienteService.deletedCliente(1L))
                .thenReturn("Cliente 1 Eliminado");

        mockMvc.perform(delete("/api/v1/clientes/1"))
                .andExpect(status().isNoContent());

        verify(clienteService).deletedCliente(1L);
    }
}