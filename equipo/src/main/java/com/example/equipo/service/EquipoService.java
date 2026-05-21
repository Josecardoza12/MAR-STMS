package com.example.equipo.service;

import com.example.equipo.exception.EquipoNotFoundException;
import com.example.equipo.model.Equipo;
import com.example.equipo.repository.EquipoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;


    public List<Equipo> listarEquipos(){
        log.info("Listando todos los equipos");
        return equipoRepository.findAll();
    }


    public Equipo obtenerPorId(Long id){
        log.info("Buscando equipo con id {}", id);
        return equipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Equipo con id {} no encontrado", id);
                    return new EquipoNotFoundException(id);
                });
    }


    public List<Equipo> findByClienteId(Long idCliente){
        log.info("Buscando equipos del cliente con id {}", idCliente);
        return  equipoRepository.findByIdCliente(idCliente);
    }


    public Equipo saveEquipo(Equipo equipo){

        if(equipo.getNumeroSerie() == null || equipo.getNumeroSerie().trim().isEmpty()){
            log.warn("No se puede registrar equipo sin número de serie");
            return equipo;
        }
        if(equipo.getMarca() == null || equipo.getMarca().isEmpty() ||
                equipo.getModelo() == null || equipo.getModelo().isEmpty()){

            log.warn("El equipo debe tener marca y modelo");
            return equipo;
        }

        log.info("Guardando equipo con número de serie {}", equipo.getNumeroSerie());
        return equipoRepository.save(equipo);
    }



    public  String deletedEquipo(Long id){
        log.warn("Eliminando equipo con id {}", id);
        equipoRepository.deleteById(id);
        log.info("Equipo {} eliminado correctamente", id);
        return "Equipo " + id + " eliminado";
    }

}
