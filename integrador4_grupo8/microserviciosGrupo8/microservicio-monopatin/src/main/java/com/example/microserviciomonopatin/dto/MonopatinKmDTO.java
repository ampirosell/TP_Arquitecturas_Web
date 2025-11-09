package com.example.microserviciomonopatin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
// DTO para el reporte de kilómetros recorridos por cada monopatín

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonopatinKmDTO {
    private Long id;
    private double kmRecorridos;
}