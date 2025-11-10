package com.example.microservicioparada.dto;

import com.example.microservicioviaje.entity.Viaje;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViajeDTO {
    private Long id;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private double kmRecorridos;
    private boolean pausa;
    private Long idMonopatin;
    private int idUsuario;

}