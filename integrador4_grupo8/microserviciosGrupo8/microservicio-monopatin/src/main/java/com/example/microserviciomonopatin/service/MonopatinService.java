package com.example.microserviciomonopatin.service;

import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microserviciomonopatin.repository.MonopatinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MonopatinService {

    @Autowired
    MonopatinRepository monopatinRepository;

    public List<Monopatin> getAll(){
        return monopatinRepository.findAll();
    }

    public Monopatin save(Monopatin monopatin){
        Monopatin monopatinNew;
        monopatinNew = monopatinRepository.save(monopatin);
        return monopatinNew;
    }
    
    public void delete(Monopatin monopatin){
        monopatinRepository.delete(monopatin);
    }

    public Monopatin update(Monopatin monopatin){
        return monopatinRepository.save(monopatin);
    }
    
    public Monopatin findById(String id) throws Exception {
        return monopatinRepository.findById(id).orElse(null);
    }
    
    public List<Monopatin> getMonopatinesCercanos(double x, double y, double distanciaCercana) {
        return monopatinRepository.getMonopatinesCercanos(x, y, distanciaCercana);
    }
}
