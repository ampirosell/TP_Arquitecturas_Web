package com.example.microservicioviaje.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
@Setter
@Getter
@Entity
public class Pausa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private LocalDateTime fechaInicio;
    @Column
    private LocalDateTime fechaFin;
    @Column
    Long pausaTotal;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "idviaje")
    private Viaje viaje;

    public Pausa() {}

    public Pausa(Pausa pausa) {
        this.id = pausa.id;
        this.fechaInicio = pausa.fechaInicio;
        this.fechaFin = pausa.fechaFin;
        this.pausaTotal = pausa.pausaTotal;
        this.viaje = pausa.viaje;
    }

    public Pausa(Long id, LocalDateTime fechaInicio, LocalDateTime fechaFin, Long pausaTotal, Viaje viaje) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.pausaTotal = pausaTotal;
        this.viaje = viaje;
    }

    public Pausa(Long id, LocalDateTime fechaInicio, LocalDateTime fechaFin, Viaje viaje) {
        this.id = id;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.pausaTotal = 0L;
        this.viaje = viaje;
    }

    public Viaje getRegistroUsoMonopatin() {
        return viaje;
    }

    @JsonIgnore
    public Long getCantidadPausa(){
        return getFechaInicio().until(getFechaFin(), ChronoUnit.MINUTES);
    }


}