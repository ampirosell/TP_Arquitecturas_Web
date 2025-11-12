package com.example.microservicioviaje.service;

//import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microservicioviaje.entity.Viaje;
import com.example.microservicioviaje.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Transactional
    public List<Viaje> getAll() {
        return viajeRepository.findAll();
    }

    @Transactional
    public Viaje save(Viaje viaje) {
        Viaje viajeNew;
        viajeNew = viajeRepository.save(viaje);
        return viajeNew;
    }

    @Transactional
    public void delete(Viaje viaje) {

        viajeRepository.delete(viaje);
    }

    @Transactional
    public Viaje update(Viaje viaje) {
        return viajeRepository.save(viaje);
    }

    @Transactional
    public Viaje findById(Long id) throws Exception {
        return viajeRepository.findById(id).orElse(null);
    }

   /* c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.

   @Transactional
    public List<Monopatin> findMonopatinesConMasDeXViajesEnAnio(int anio, long minViajes){
        return viajeRepository.findMonopatinesConMasDeXViajesEnAnio(anio, minViajes);
    }*/
}
