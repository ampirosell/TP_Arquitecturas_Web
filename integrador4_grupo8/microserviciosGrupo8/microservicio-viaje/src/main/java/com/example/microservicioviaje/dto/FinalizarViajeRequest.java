package com.example.microservicioviaje.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinalizarViajeRequest {

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    private Double kilometrosRecorridos;

    @NotNull
    private Long paradaId;

    @NotNull
    private Double latitud;
    @NotNull
    private Double longitud;
}

