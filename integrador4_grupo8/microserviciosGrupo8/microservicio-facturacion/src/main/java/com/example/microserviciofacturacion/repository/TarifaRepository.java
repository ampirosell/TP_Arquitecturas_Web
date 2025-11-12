package com.example.microserviciofacturacion.repository;

import com.example.microserviciofacturacion.entity.Tarifa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarifaRepository extends JpaRepository<Tarifa, Long> {
    Tarifa findFirstByActivaTrue();
}