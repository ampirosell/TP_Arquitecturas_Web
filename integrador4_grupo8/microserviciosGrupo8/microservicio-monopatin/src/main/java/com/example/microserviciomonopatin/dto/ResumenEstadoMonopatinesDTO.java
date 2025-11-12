package com.example.microserviciomonopatin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumenEstadoMonopatinesDTO {
    private long disponibles;
    private long enUso;
    private long enMantenimiento;
}

