package com.example.microserviciomonopatin.repository;

import com.example.microserviciomonopatin.entity.Monopatin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonopatinRepository extends MongoRepository<Monopatin, String>{

    @Query("{ $expr: { $lt: [ { $sqrt: { $add: [ { $pow: [ { $subtract: [ '$latitud', ?0 ] }, 2 ] }, { $pow: [ { $subtract: [ '$longitud', ?1 ] }, 2 ] } ] } }, ?2 ] }, latitud: { $exists: true }, longitud: { $exists: true } }")
    List<Monopatin> getMonopatinesCercanos(Double latitud, Double longitud, Double distancia);
}
