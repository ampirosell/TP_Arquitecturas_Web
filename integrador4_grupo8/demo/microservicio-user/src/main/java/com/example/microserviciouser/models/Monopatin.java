package com.example.microserviciouser.models;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Monopatin {
    private enum EstadoMonopatin { LIBRE, MANTENIMIENTO, EN_USO};
    private Long kmRecorridos;
    private Long viajeId; //el que esta en curso
    private Double latitud;
    private Double longitud;


}
