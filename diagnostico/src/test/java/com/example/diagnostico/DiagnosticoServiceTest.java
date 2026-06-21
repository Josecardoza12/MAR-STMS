package com.example.diagnostico;

import com.example.diagnostico.client.OrdenTrabajoClient;
import com.example.diagnostico.exception.DiagnosticoNotFoundException;
import com.example.diagnostico.model.Diagnostico;
import com.example.diagnostico.repository.DiagnosticoRepository;
import com.example.diagnostico.service.DiagnosticoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiagnosticoServiceTest {

    @Mock
    DiagnosticoRepository repository;

    @Mock
    OrdenTrabajoClient otClient;

    @InjectMocks
    DiagnosticoService service;

    // aquí van los tests
    @Test
    void findById_ok() {
        Diagnostico d = new Diagnostico();
        d.setDiagnosticoId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(d));

        Diagnostico result = service.findById(1L);

        assertEquals(1L, result.getDiagnosticoId());
        verify(repository).findById(1L);
    }
    @Test
    void findById_notFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(DiagnosticoNotFoundException.class,
                () -> service.findById(1L));

        verify(repository).findById(1L);
    }
    @Test
    void save_ok() {
        Diagnostico d = new Diagnostico();
        d.setOtId(10L);

        when(repository.save(d)).thenReturn(d);

        Diagnostico result = service.save(d, "token-falso");

        assertNotNull(result);
        verify(repository).save(d);
    }
    @Test
    void update_ok() {
        Diagnostico existing = new Diagnostico();
        existing.setDiagnosticoId(1L);
        existing.setDescripcion("vieja");

        Diagnostico nuevo = new Diagnostico();
        nuevo.setDescripcion("nueva");

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Diagnostico.class))).thenAnswer(inv -> inv.getArgument(0));

        Diagnostico result = service.update(1L, nuevo, "token-falso");

        assertEquals("nueva", result.getDescripcion());
        verify(repository).findById(1L);
        verify(repository).save(any(Diagnostico.class));
    }

}

