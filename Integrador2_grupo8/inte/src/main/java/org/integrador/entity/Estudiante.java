package org.integrador.entity;

import jakarta.persistence.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "estudiante")
public class Estudiante {
    public Estudiante(int dni, String nombre, String apellido, Integer edad, String genero, String ciudadDeResidencia, String numeroLU) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.genero = genero;

        this.ciudadDeResidencia = ciudadDeResidencia;
        this.numeroLU = numeroLU;
        this.carreras = new ArrayList<>();
    }

    public Estudiante(){
        this.carreras = new ArrayList<>();
    }
    @Id
    @Column(name = "dni")
    private int dni;
    
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "apellido", nullable = false )
    private String apellido;
    
    @Column(name = "edad", nullable = false)
    private Integer edad;
    
    @Column(name = "genero", nullable = false)
    private String genero;

    @Column(name = "ciudad_residencia", nullable = false)
    private String ciudadDeResidencia;
    
    @Column(name = "numero_lu", nullable = false)
    private String numeroLU;
    
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstudianteDeCarrera> carreras;






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

    public int getDni() {
        return dni;
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
