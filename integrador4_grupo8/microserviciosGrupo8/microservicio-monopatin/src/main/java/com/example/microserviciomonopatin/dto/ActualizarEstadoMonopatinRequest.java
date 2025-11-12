package com.example.microserviciomonopatin.dto;

import com.example.microserviciomonopatin.entity.EstadoMonopatin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarEstadoMonopatinRequest {
    @NotNull
    private EstadoMonopatin estado;
    private Long viajeId;
}

