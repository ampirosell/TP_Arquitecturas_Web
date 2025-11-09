package com.example.microserviciocuenta.repository;

import com.example.microserviciocuenta.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    //ejercicio B
    @Query("update Cuenta c set c.cuentaActiva = :estado  where c.idUsuario = :id")
    Cuenta actualizarEstadoCuenta(@Param("id") Long id, @Param("cuentaActiva") Boolean cuentaActiva);
}
