package com.example.microserviciomonopatin.repository;

import com.example.microserviciomonopatin.entity.EstadoMonopatin;
import com.example.microserviciomonopatin.entity.Monopatin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonopatinRepository extends MongoRepository<Monopatin, Long> {

    @Query("{ $expr: { $lt: [ { $sqrt: { $add: [ { $pow: [ { $subtract: [ '$latitud', ?0 ] }, 2 ] }, { $pow: [ { $subtract: [ '$longitud', ?1 ] }, 2 ] } ] } }, ?2 ] }, latitud: { $exists: true }, longitud: { $exists: true } }")
    List<Long> getMonopatinesCercanos(Double latitud, Double longitud, Double distancia);

    long countByEstadoMonopatin(EstadoMonopatin estadoMonopatin);

}