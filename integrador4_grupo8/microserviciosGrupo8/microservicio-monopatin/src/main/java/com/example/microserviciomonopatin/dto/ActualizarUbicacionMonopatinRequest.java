package com.example.microserviciomonopatin.dto;

import lombok.Data;

@Data
public class ActualizarUbicacionMonopatinRequest {
    private Double latitud;
    private Double longitud;
    private Long paradaId;
}

