package com.example.microserviciocuenta.repository;

import com.example.microserviciocuenta.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    // ejercicio B
    @Modifying
    @Transactional
    @Query("update Cuenta c set c.cuentaActiva = :estado where c.idUsuario = :id")
    int actualizarEstadoCuenta(@Param("id") Long id, @Param("estado") Boolean estado);

    @Modifying
    @Transactional
    @Query("update Cuenta c set c.cuentaActiva = :estado where c.idCuenta = :idCuenta")
    int actualizarEstadoCuentaPorId(@Param("idCuenta") Long idCuenta, @Param("estado") Boolean estado);

    Optional<Cuenta> findByIdUsuario(Long idUsuario);
}
