package com.example.microserviciomonopatin.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Monopatin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMonopatin;
    private EstadoMonopatin estadoMonopatin;
    private Long kmRecorridos;
    private Long viajeId; //el que esta en curso
    private Double latitud;
    private Double longitud;

    public Monopatin(EstadoMonopatin estadoMonopatin) {
        this.estadoMonopatin = estadoMonopatin;
    }
}
