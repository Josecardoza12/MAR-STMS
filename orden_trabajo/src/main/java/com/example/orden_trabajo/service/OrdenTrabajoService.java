package com.example.orden_trabajo.service;

import com.example.orden_trabajo.exception.OrdenTrabajoNotFoundException;
import com.example.orden_trabajo.model.OrdenTrabajo;
import com.example.orden_trabajo.repository.OrdenTrabajoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class OrdenTrabajoService {
    @Autowired
    private OrdenTrabajoRepository ordenTrabajoRepository;


    public List<OrdenTrabajo> listarOt(){
        log.info("Listando todas las ot");
        return ordenTrabajoRepository.findAll();
    }


    public OrdenTrabajo obtenerOtPorId(Long id){
        log.info("Buscando orden de trabajo con id {}", id);
        return ordenTrabajoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("OT con id {} no encontrado", id);
                    return new OrdenTrabajoNotFoundException(id);
                });
    }



    public List<OrdenTrabajo> findByClienteId(Long idCliente){
        log.info("Buscando ordenes de trabajo del cliente con id {}", idCliente);

        List<OrdenTrabajo> clienteOt = ordenTrabajoRepository.findByIdCliente(idCliente);
        if(clienteOt.isEmpty()){
            log.info("El cliente {} no tiene ordenes de trabajo registradas", idCliente);
        }else{
            log.info("Se encontraron {} ordenes de trabajo para el cliente {}", clienteOt.size(), idCliente);
        }

        return clienteOt;
    }

    //Obtener por id de Equipos
    public List<OrdenTrabajo> findByIdEquipo(Long idEquipo){
        log.info("Buscando ordenes de trabajo del equipo con id {}", idEquipo);

        List<OrdenTrabajo> EquipoOt = ordenTrabajoRepository.findByIdEquipo(idEquipo);
        if(EquipoOt.isEmpty()){
            log.info("El equipo {} no tiene ordenes de trabajo registradas", idEquipo);
        }else{
            log.info("Se encontraron {} ordenes de trabajo para el equipo {}", EquipoOt.size(), idEquipo);
        }

        return EquipoOt;
    }


    public OrdenTrabajo saveOt(OrdenTrabajo ot){

        if(ot.getEstado() == null || ot.getEstado().isEmpty()){
            ot.setEstado("INGRESADA");
            log.info("Se asigna estado inicial 'INGRESADA' a la OT");
        }
        if(ot.getEstado().equals("ENTREGADA") &&
                (ot.getEstadoPago() == null || !ot.getEstadoPago().equals("PAGADA"))){

            log.warn("No se puede entregar una OT que no está pagada");

            return ot;
        }

        log.info("Guardando ot con numeroOt {}", ot.getNumeroOt());
        return ordenTrabajoRepository.save(ot);
    }



    public String deletedOt(Long id){
        log.warn("Eliminando ot con id {}", id);
        ordenTrabajoRepository.deleteById(id);
        log.info("Orden de trabajo {} eliminada correctamente", id);
        return "Orden de trabajo " + id + " eliminada";
    }


}
