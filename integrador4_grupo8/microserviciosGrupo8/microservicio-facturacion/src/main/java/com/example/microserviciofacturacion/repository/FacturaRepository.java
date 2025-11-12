package com.example.microserviciofacturacion.repository;

import com.example.microserviciofacturacion.entity.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByFechaEmisionBetween(LocalDateTime desde, LocalDateTime hasta);
}