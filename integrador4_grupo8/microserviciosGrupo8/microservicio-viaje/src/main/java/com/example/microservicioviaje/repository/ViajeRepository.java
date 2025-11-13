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



    /*
    *\c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.
     */
    @Query("SELECT v.idMonopatin " +
            "FROM Viaje v " +
            "WHERE FUNCTION('YEAR', v.fechaInicio) = :anio " +
            "GROUP BY v.idMonopatin " +
            "HAVING COUNT(v) > :minViajes")
    List<Long> findMonopatinesConMasViajes(
            @Param("anio") int anio,
            @Param("minViajes") long minViajes);



    @Query("SELECT v.idUsuario " +
            "FROM Viaje v " +
            "WHERE v.fechaInicio BETWEEN :desde AND :hasta " +
            "GROUP BY v.idUsuario " +
            "ORDER BY COUNT(v) DESC")
    List<Viaje> findByFechaInicioBetween(LocalDateTime desde, LocalDateTime hasta);
    //ejercicio E
    @Query("SELECT v.idUsuario " +
            "FROM Viaje v " +
            "WHERE v.fechaInicio BETWEEN :desde AND :hasta " +
            "GROUP BY v.idUsuario " +
            "ORDER BY COUNT(v) DESC")
    List<Long> findUsuariosConMasViajesPorPeriodo(
            @Param("desde") LocalDateTime desde,
            @Param("hasta") LocalDateTime hasta);

    //ejercicioH
    List<Viaje> findByUsuarioAndFechaInicioBetween(Long idUsuario, LocalDateTime desde, LocalDateTime hasta);

    List<Viaje> findByCuentaAndFechaInicioBetween(Long idCuenta, LocalDateTime desde, LocalDateTime hasta);
}
