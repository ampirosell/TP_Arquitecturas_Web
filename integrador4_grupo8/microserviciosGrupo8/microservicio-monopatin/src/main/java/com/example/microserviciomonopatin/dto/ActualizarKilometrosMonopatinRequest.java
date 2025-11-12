package com.example.microserviciomonopatin.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActualizarKilometrosMonopatinRequest {
    @NotNull
    @DecimalMin(value = "0.0", inclusive = true, message = "Los kilómetros añadidos no pueden ser negativos")
    private Double kilometrosRecorridos;
}

