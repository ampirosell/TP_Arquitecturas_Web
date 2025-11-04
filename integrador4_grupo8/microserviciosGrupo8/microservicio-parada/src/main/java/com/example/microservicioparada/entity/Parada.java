package com.example.microservicioparada.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idParada;
    private String direccion;
    private String nombre;
    // Coordenadas geográficas de la parada, x corresponde a longitud e y a latitud
    private Double longitud;
    private Double latitud;


    //  Lista de IDs de monopatines asociados a esta parada
    private List<Long> monopatinIds;
}
