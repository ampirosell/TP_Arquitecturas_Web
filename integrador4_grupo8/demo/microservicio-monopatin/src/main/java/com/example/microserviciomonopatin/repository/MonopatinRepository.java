package com.example.microserviciomonopatin.repository;

import com.example.microserviciomonopatin.entity.Monopatin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonopatinRepository extends JpaRepository<Monopatin,Long>{

    @Query("SELECT p FROM Monopatin p WHERE SQRT(POWER(p.latitud - :latitud, 2) + POWER(p.longitud - :longitud, 2)) < :distancia")
    List<Monopatin> getMonopatinesCercanos(
            @Param("latitud") Double latitud,
            @Param("longitud") Double longitud,
            @Param("distancia") Double distancia
    );
}
