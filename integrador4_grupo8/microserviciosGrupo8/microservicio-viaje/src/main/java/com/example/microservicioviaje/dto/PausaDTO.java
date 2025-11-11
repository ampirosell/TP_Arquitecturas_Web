package com.example.microservicioviaje.dto;


import com.example.microservicioviaje.entity.Pausa;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class PausaDTO implements Serializable {
    private Long id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;


    public PausaDTO(Long id, LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public PausaDTO(Pausa p) {
        this.id = p.getId();
        this.fechaInicio = p.getFechaInicio();
        this.fechaFin = p.getFechaFin();
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }


}