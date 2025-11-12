package com.example.microservicioviaje.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaRemote {
    private Long idCuenta;
    private Double monto;
    private boolean cuentaActiva;
}

