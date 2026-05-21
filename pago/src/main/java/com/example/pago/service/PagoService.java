package com.example.pago.service;

import com.example.pago.exception.PagoNotFoundException;
import com.example.pago.model.Pago;
import com.example.pago.repository.PagoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;



    public List<Pago> listarTodos() {
        log.info("Listando todos los pagos");
        return pagoRepository.findAll();
    }

    public Pago obtenerPorId(Long id) {
        log.info("Buscando pago con id {}", id);
        return pagoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Pago con id {} no encontrado", id);
                    return new PagoNotFoundException(id);
                });
    }

    public List<Pago> obtenerPorOtId(Long otId) {
        log.info("Buscando pagos para OT {}", otId);
        return pagoRepository.findByOtId(otId);
    }

    public Pago registrar(Pago pago, String token) {
        log.info("Registrando pago para OT {}", pago.getOtId());
        pago.setFecha(LocalDate.now());
        pago.setEstado("pagado");
        Pago saved = pagoRepository.save(pago);
        log.info("Pago registrado con id {}", saved.getId());



        return saved;
    }

    public Pago actualizar(Long id, Pago pagoActualizado) {
        log.info("Actualizando pago con id {}", id);
        Pago pago = obtenerPorId(id);
        pago.setMonto(pagoActualizado.getMonto());
        pago.setFormaPago(pagoActualizado.getFormaPago());
        pago.setEstado(pagoActualizado.getEstado());
        Pago updated = pagoRepository.save(pago);
        log.info("Pago {} actualizado correctamente", id);
        return updated;
    }

    public String eliminar(Long id) {
        log.info("Eliminando pago con id {}", id);
        obtenerPorId(id);
        pagoRepository.deleteById(id);
        log.info("Pago {} eliminado correctamente", id);
        return "Pago " + id + " eliminado";
    }
}