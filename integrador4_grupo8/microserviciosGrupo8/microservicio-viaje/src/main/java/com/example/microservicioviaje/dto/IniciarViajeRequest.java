package com.example.microservicioviaje.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IniciarViajeRequest {

    @NotNull
    private Long idCuenta;

    @NotNull
    private Long idUsuario;

    @NotNull
    private Long idMonopatin;
}

