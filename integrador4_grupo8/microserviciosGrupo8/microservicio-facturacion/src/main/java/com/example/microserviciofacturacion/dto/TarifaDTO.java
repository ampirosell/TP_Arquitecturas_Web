package com.example.microserviciofacturacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDTO {
    private Long id;
    private double precioPorKm;
    private double precioPorMinuto;
    private double tarifaExtraPausa; // extra si se reanuda luego de 15 min
    private LocalDate fechaInicioVigencia; // desde cuándo rige esta tarifa
}
