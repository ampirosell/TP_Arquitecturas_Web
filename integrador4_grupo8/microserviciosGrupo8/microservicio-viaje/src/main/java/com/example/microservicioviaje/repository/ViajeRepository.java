package com.example.microservicioviaje.repository;

//import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microservicioviaje.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Long> {
/*
    @Query("SELECT v.idMonopatin, COUNT(v) " +
            "FROM Viaje v " +
            "WHERE FUNCTION('YEAR', v.fecha) = :anio " +
            "GROUP BY v.idMonopatin " +
            "HAVING COUNT(v) > :minViajes")
    List<Monopatin> findMonopatinesConMasDeXViajesEnAnio(
            @Param("anio") int anio,
            @Param("minViajes") long minViajes);
    /*


    @Query("select NEW com.microservicio_user.dto.ReporteKilometrajeDTO(v.idMonopatin,sum(v.kmRecorridos)) from Viaje v where v.kmRecorridos >= :umbral group by v.idMonopatin")
    List<ReporteKilometrajeDTO> getReporteKilometraje(long umbral);

    @Query("select NEW com.microservicio_user.dto.ReporteKilometrajeDTO(v.idMonopatin,sum(v.kmRecorridos),sum(p.pausaTotal)) from Viaje v join Pausa p on v.id=p.id where v.kmRecorridos >= :umbral group by v.idMonopatin")
    List<ReporteKilometrajeDTO> getReporteKilometrajeConPausas(long umbral);

    @Query("SELECT SUM(p.valor + p.valorPorPausaExtendida) FROM Viaje v JOIN v.precio p WHERE FUNCTION('YEAR', v.fecha) = :anio AND FUNCTION('MONTH', v.fecha) BETWEEN :mesInicio AND :mesFin")
    Integer getFacturadoEntreMeses(int anio, int mesInicio, int mesFin);
*/

}
