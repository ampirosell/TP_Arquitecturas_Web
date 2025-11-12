package com.example.microservicioviaje.repository;


import com.example.microservicioviaje.entity.Pausa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PausaRepository extends JpaRepository<Pausa, Long> {

    @Query("SELECT COALESCE(SUM(p.pausaTotal),0) FROM Pausa p WHERE p.viaje.idViaje = :viajeId")
    Long obtenerMinutosPausaPorViaje(@Param("viajeId") Long viajeId);
}
