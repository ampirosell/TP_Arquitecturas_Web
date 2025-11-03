package com.example.microserviciomonopatin.service;

import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microserviciomonopatin.repository.MonopatinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MonopatinService {

    @Autowired
    MonopatinRepository monopatinRepository;

    @Transactional
    public List<Monopatin> getAll(){
        return monopatinRepository.findAll();
    }

    @Transactional
    public Monopatin save(Monopatin monopatin){
        Monopatin monopatinNew;
        monopatinNew = monopatinRepository.save(monopatin);
        return monopatinNew;
    }
    @Transactional
    public void delete(Monopatin monopatin){

        monopatinRepository.delete(monopatin);
    }

    @Transactional
    public Monopatin update(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }
    @Transactional
    public Monopatin findById(Long id) throws Exception {
        return monopatinRepository.findById(id).orElse(null);
    }
    @Transactional
    public List<Monopatin> getMonopatinesCercanos(double x, double y, double distanciaCercana) {

        return monopatinRepository.getMonopatinesCercanos(x, y, distanciaCercana);
    }
}
