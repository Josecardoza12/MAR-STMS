package com.example.cliente;



import com.example.cliente.exception.ClienteNotFoundException;
import com.example.cliente.model.Cliente;
import com.example.cliente.repository.ClienteRepository;
import com.example.cliente.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deberiaListarClientes() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");

        when(clienteRepository.findAll())
                .thenReturn(List.of(cliente));

        List<Cliente> resultado = clienteService.listarClientes();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Jose Cardoza", resultado.get(0).getNombre());

        verify(clienteRepository).findAll();
    }

    @Test
    void deberiaRetornarClienteCuandoExiste() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");

        when(clienteRepository.findById(1L))
                .thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Jose Cardoza", resultado.getNombre());
        assertEquals("12345678-9", resultado.getRut());

        verify(clienteRepository).findById(1L);
    }


    @Test
    void deberiaLanzarExcepcionCuandoClienteNoExiste() {
        when(clienteRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ClienteNotFoundException.class,
                () -> clienteService.obtenerPorId(99L)
        );

        verify(clienteRepository).findById(99L);
    }

    @Test
    void deberiaGuardarClienteValido() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("12345678-9");
        cliente.setTelefono("+56912345678");
        cliente.setDireccion("Melipilla");
        cliente.setTipo_Cliente("Frecuente");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setId(1L);
        clienteGuardado.setNombre("Jose Cardoza");
        clienteGuardado.setRut("12345678-9");
        clienteGuardado.setTelefono("+56912345678");
        clienteGuardado.setDireccion("Melipilla");
        clienteGuardado.setTipo_Cliente("Frecuente");

        when(clienteRepository.save(cliente))
                .thenReturn(clienteGuardado);

        Cliente resultado = clienteService.saveCliente(cliente);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Jose Cardoza", resultado.getNombre());
        assertEquals("12345678-9", resultado.getRut());

        verify(clienteRepository).save(cliente);
    }


    @Test
    void noDeberiaGuardarClienteConRutInvalido() {
        Cliente cliente = new Cliente();
        cliente.setNombre("Jose Cardoza");
        cliente.setRut("123");

        Cliente resultado = clienteService.saveCliente(cliente);

        assertNotNull(resultado);
        assertNull(resultado.getId());

        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void deberiaEliminarClientePorId() {
        doNothing().when(clienteRepository).deleteById(1L);

        String resultado = clienteService.deletedCliente(1L);

        assertEquals("Cliente 1 Eliminado", resultado);

        verify(clienteRepository).deleteById(1L);
    }
}