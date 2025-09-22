package org.integrador.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Estudiante {


    @Id
    private Long estudianteId;
    private String nombre;
    private String apellido;
    private Integer edad;
    private String genero;
    private String dni; //sino el numero es muy grande
    private String ciudadDeResidencia;
    private String numeroLU; //dejamos string sino el numero es muy grande
    private List<EstudianteDeCarrera> carreras;

    public Long getId() {
        return estudianteId;
    }

    public void setId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCiudadDeResidencia() {
        return ciudadDeResidencia;
    }

    public void setCiudadDeResidencia(String ciudadDeResidencia) {
        this.ciudadDeResidencia = ciudadDeResidencia;
    }

    public String getNumeroLU() {
        return numeroLU;
    }

    public void setNumeroLU(String numeroLU) {
        this.numeroLU = numeroLU;
    }

    public List<EstudianteDeCarrera> getCarreras() {
        return carreras;
    }

    public void setCarreras(List<EstudianteDeCarrera> carreras) {
        this.carreras = carreras;
    }
}
