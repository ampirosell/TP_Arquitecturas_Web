package com.example.microservicioviaje.model;

import com.example.microservicioviaje.model.enums.EstadoMonopatinRemote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonopatinRemote {
    private Long idMonopatin;
    private EstadoMonopatinRemote estadoMonopatin;
    private Double latitud;
    private Double longitud;
    private Long paradaId;
    private Long viajeId;
    private Double kmRecorridos;
}

