package com.example.microservicioviaje.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
public class Viaje {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idViaje;
    @Column
    private LocalDateTime fechaInicio;
    @Column
    private LocalDateTime fechaFin;
    @Column
    private double kmRecorridos;
    @Column
    private boolean pausa;
    @JsonIgnore
    private Long idMonopatin;
    private Long idUsuario;
    private Long idCuenta;

    /*@Setter
    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "viaje", cascade = CascadeType.ALL)
    private List<Pausa> pausas;

    @Setter
    @Getter
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "precio_id")
    private Precio precio;
*/

    public Viaje(Viaje viaje) {
        this.idViaje = viaje.idViaje;
        this.idMonopatin = viaje.idMonopatin;
        this.fechaInicio = viaje.fechaInicio;
        this.fechaFin = viaje.fechaFin;
        this.kmRecorridos = viaje.kmRecorridos;
        //this.pausas = viaje.pausas;
        //this.precio = viaje.precio;
    }

    public Viaje() {

    }

    public Viaje(Long idViaje, Long idmonopatin, LocalDateTime fechaInicio, LocalDateTime fechaFin, double kilometrosRecorridos) {
        this.idViaje = idViaje;
        this.idMonopatin = idmonopatin;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.kmRecorridos = kilometrosRecorridos;
       // this.pausas = pausas;
        //this.precio = precio;
    }

    public Long getMonopatin() {
        return this.idMonopatin;
    }

    public void setMonopatin(Long monopatin) {
        this.idMonopatin = monopatin;
    }

}