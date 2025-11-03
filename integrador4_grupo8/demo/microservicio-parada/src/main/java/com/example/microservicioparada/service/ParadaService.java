package com.example.microservicioparada.service;

import com.example.microservicioparada.entity.Parada;
import com.example.microservicioparada.repository.ParadaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParadaService {

    @Autowired
    ParadaRepository paradaRepository;

    @Transactional
    public List<Parada> getAll() {
        return paradaRepository.findAll();
    }

    @Transactional
    public Parada save(Parada parada) {
        Parada paradaNew;
        paradaNew = paradaRepository.save(parada);
        return paradaNew;
    }

    @Transactional
    public void delete(Parada parada) {

        paradaRepository.delete(parada);
    }

    @Transactional
    public Parada update(Parada parada) {
        return paradaRepository.save(parada);
    }

    @Transactional
    public Parada findById(Long id) throws Exception {
        return paradaRepository.findById(id).orElse(null);
    }

    @Transactional
    public List<Parada> getMonopatinesCercanos(double x, double y, double distanciaCercana) {

        return paradaRepository.getParadasCercanas(x, y, distanciaCercana);
    }
}
