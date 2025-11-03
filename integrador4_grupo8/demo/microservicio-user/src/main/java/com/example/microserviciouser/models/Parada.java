package com.example.microserviciouser.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parada {

    private String direccion;
    private String  nombre;
    private Double latitud;
    private Double longitud;
}
