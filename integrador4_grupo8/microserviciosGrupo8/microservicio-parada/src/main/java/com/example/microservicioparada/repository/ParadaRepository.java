package com.example.microservicioparada.repository;

import com.example.microservicioparada.entity.Parada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParadaRepository extends JpaRepository<Parada, Long> {

    @Query("SELECT p FROM Parada p " +
            "WHERE SQRT(POWER(p.latitud - :latitud, 2) + POWER(p.longitud - :longitud, 2)) < :distancia")
    List<Parada> getParadasCercanas(
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("distancia") Double distancia
    );
}
