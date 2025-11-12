package com.example.microserviciofacturacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
 @Data
@Entity
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idCuenta;
    private Long idViaje;
    private double kmRecorridos;
    private long minutosTotales;
    private double montoTotal;
    private LocalDateTime fechaEmision;


}
