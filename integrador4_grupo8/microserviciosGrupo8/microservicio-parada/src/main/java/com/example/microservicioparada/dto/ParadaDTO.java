package com.example.microservicioparada.dto;

import com.example.microservicioparada.entity.Parada;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParadaDTO {
    private Long id;
    private double longitud; // x
    private double latitud;  // y
    private List<Long> monopatinIds;

    public ParadaDTO(Parada parada) {
        this.id = parada.getIdParada();
        this.longitud = parada.getLongitud();
        this.latitud = parada.getLatitud();
        this.monopatinIds = parada.getMonopatinIds();
    }
}