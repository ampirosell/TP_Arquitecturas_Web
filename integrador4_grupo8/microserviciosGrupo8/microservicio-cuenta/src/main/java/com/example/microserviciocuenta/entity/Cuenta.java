package com.example.microserviciocuenta.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import lombok.*;


import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Cuenta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuenta;
    private Long idUsuario;
    private Double monto;
    private TipoCuenta tipo;
    private Date fecha_alta;
    private boolean cuentaActiva;


    public Cuenta(TipoCuenta tipo) {
        this.tipo = tipo;
    }


}
