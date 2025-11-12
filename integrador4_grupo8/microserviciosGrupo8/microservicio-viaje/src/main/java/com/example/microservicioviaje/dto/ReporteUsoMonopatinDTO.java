package com.example.microservicioviaje.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReporteUsoMonopatinDTO {
    private Long idMonopatin;
    private double kilometros;
    private long minutosConPausa;
    private long minutosPausa;
    private long minutosSinPausa;
}

