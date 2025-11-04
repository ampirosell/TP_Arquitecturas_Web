package com.example.microserviciocuenta.dto;

import com.example.microserviciocuenta.entity.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
//Esta clase no representa un monopatín, sino un resumen de cuántos monopatines están operativos y cuántos en mantenimiento y devolver los datos necesarios.
// al no ser una entidad  como tal y no tener relaciones ej: OneToMany, es mas facil de serializar(convertir a json).

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDTO implements Serializable {

    private Double monto;
    private Date fecha_alta;
    private boolean estado;

}
