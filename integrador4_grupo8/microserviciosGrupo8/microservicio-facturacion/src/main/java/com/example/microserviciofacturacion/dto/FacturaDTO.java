package com.example.microserviciofacturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {
    private Long id;
    private Long idCuenta;       // cuenta asociada a la factura
    private Long idViaje;        // viaje que originó la factura
    private double montoTotal;
    private LocalDateTime fechaEmision;
    private boolean pagada;
}
