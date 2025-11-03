package com.example.microserviciomonopatin.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "monopatines")
@Getter
@Setter
@NoArgsConstructor
public class Monopatin {
    @Id
    private String idMonopatin;
    private EstadoMonopatin estadoMonopatin;
    private Long kmRecorridos;
    private Long viajeId; //el que esta en curso
    private Double latitud;
    private Double longitud;

    public Monopatin(EstadoMonopatin estadoMonopatin) {
        this.estadoMonopatin = estadoMonopatin;
    }
}
