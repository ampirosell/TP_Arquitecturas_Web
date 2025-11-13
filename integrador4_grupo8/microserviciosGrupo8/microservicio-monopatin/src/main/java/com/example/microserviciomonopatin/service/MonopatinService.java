package com.example.microserviciomonopatin.service;


import com.example.microserviciomonopatin.dto.ActualizarKilometrosMonopatinRequest;
import com.example.microserviciomonopatin.dto.MonopatinKmDTO;
import com.example.microserviciomonopatin.dto.ResumenEstadoMonopatinesDTO;
import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microserviciomonopatin.entity.EstadoMonopatin;
import com.example.microserviciomonopatin.repository.MonopatinRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonopatinService {

    @Autowired
    MonopatinRepository monopatinRepository;

    public List<Monopatin> getAll(){
        return monopatinRepository.findAll();
    }

    public Monopatin save(Monopatin monopatin){
        Monopatin monopatinNew;
        if (monopatin.getKmRecorridos() == null) {
            monopatin.setKmRecorridos(0D);
        }
        monopatinNew = monopatinRepository.save(monopatin);
        return monopatinNew;
    }
    
    public void delete(Monopatin monopatin){
        monopatinRepository.delete(monopatin);
    }

    public Monopatin update(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }
    
    public Monopatin findById(Long id) throws Exception {
        return monopatinRepository.findById(id).orElse(null);
    }

    public List<Monopatin> getMonopatinesCercanos(double latitud, double longitud, double distanciaCercana) {
        return monopatinRepository.getMonopatinesCercanos(latitud, longitud, distanciaCercana);
    }

    public Monopatin actualizarEstado(Long id, EstadoMonopatin nuevoEstado, Long viajeId) {
        return monopatinRepository.findById(id)
                .map(monopatin -> {
                    monopatin.setEstadoMonopatin(nuevoEstado);
                    monopatin.setViajeId(viajeId);
                    if (nuevoEstado == EstadoMonopatin.EN_USO) {
                        monopatin.setParadaId(null);
                    }
                    return monopatinRepository.save(monopatin);
                })
                .orElse(null);
    }

    public Monopatin actualizarUbicacion(Long id, Double latitud, Double longitud, Long paradaId) {
        return monopatinRepository.findById(id)
                .map(monopatin -> {
                    if (latitud != null) {
                        monopatin.setLatitud(latitud);
                    }
                    if (longitud != null) {
                        monopatin.setLongitud(longitud);
                    }
                    monopatin.setParadaId(paradaId);
                    return monopatinRepository.save(monopatin);
                })
                .orElse(null);
    }

    public Monopatin registrarMantenimiento(Long id) {
        return actualizarEstado(id, EstadoMonopatin.MANTENIMIENTO, null);
    }

    public Monopatin finalizarMantenimiento(Long id) {
        return actualizarEstado(id, EstadoMonopatin.LIBRE, null);
    }

    public ResumenEstadoMonopatinesDTO obtenerResumenEstados() {
        long libres = monopatinRepository.countByEstadoMonopatin(EstadoMonopatin.LIBRE);
        long enUso = monopatinRepository.countByEstadoMonopatin(EstadoMonopatin.EN_USO);
        long enMantenimiento = monopatinRepository.countByEstadoMonopatin(EstadoMonopatin.MANTENIMIENTO);
        return new ResumenEstadoMonopatinesDTO(libres, enUso, enMantenimiento);
    }

    public Monopatin actualizarKilometros(Long id, Double kilometrosRecorridos) {
        return monopatinRepository.findById(id)
                .map(monopatin -> {
                    double acumulado = monopatin.getKmRecorridos() != null ? monopatin.getKmRecorridos() : 0D;
                    monopatin.setKmRecorridos(acumulado + kilometrosRecorridos);
                    return monopatinRepository.save(monopatin);
                })
                .orElse(null);
    }
/*  ESTO NO VA SI LO PONEMOS EN EL VIAJE
    //ejercicio a
    public List<MonopatinKmDTO> generarReporteKm() {
        return monopatinRepository.findAll().stream()
                .map(m -> new MonopatinKmDTO(m.getIdMonopatin(), m.getKmRecorridos()))
                .collect(Collectors.toList());
    }

*/

}
