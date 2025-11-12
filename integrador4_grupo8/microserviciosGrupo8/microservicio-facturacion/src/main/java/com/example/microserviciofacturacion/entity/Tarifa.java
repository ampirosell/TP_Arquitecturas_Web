package com.example.microserviciofacturacion.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Tarifa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double precioPorKm;
    private double precioPorMinuto;
    private double recargoPausa; // si excede los 15 min
    private LocalDate fechaInicio; // fecha desde que rige esta tarifa
    private boolean activa;


}
