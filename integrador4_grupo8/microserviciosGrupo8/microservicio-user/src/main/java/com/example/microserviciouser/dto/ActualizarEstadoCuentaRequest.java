package com.example.microserviciouser.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ActualizarEstadoCuentaRequest {
    private boolean activa;
}
