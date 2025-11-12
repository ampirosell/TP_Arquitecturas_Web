package com.example.microservicioviaje.repository;

//import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microservicioviaje.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
    @Query("SELECT new com.example.microservicioviaje.dto.MonopatinViajesDTO(v.idMonopatin, COUNT(v)) " +
            "FROM Viaje v " +
            "WHERE FUNCTION('YEAR', v.fechaInicio) = :anio " +
            "GROUP BY v.idMonopatin " +
            "HAVING COUNT(v) > :minViajes")
    List<com.example.microservicioviaje.dto.MonopatinViajesDTO> findMonopatinesConMasViajes(
            @Param("anio") int anio,
            @Param("minViajes") long minViajes);

    List<Viaje> findByFechaInicioBetween(LocalDateTime desde, LocalDateTime hasta);

}
