package com.example.microserviciofacturacion.repository;

import com.example.microserviciofacturacion.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Optional<Tarifa> findTopByFechaInicioLessThanEqualOrderByFechaInicioDesc(LocalDate fecha);

    @Modifying
    @Query("update Tarifa t set t.activa = false where t.id <> :idTarifa")
    void desactivarOtrasTarifas(@Param("idTarifa") Long idTarifa);

    @Modifying
    @Query("update Tarifa t set t.activa = true where t.fechaInicio <= :fecha and t.activa = false")
    void activarTarifasVigentes(@Param("fecha") LocalDate fecha);
}