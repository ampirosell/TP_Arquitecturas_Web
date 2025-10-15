package org.integrador.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carrera")
public class Carrera {
    public Carrera(Long carreraId, String nombre, Integer duracion) {
        this.carreraId = carreraId;
        this.nombre = nombre;
        this.duracion = duracion;
        this.estudiantes = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "carrera_id")
    private Long carreraId;
    
    @Column(name = "nombre", nullable = false, unique = true)
    private String nombre;
    
    @Column(name = "duracion")
    private Integer duracion; // en años
    
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EstudianteDeCarrera> estudiantes;

    // Constructores
    public Carrera() {}

    public Carrera(String nombre, Integer duracion) {
        this.nombre = nombre;
        this.duracion = duracion;
    }

    // Getters y Setters
    public Long getId() {
        return carreraId;
    }

    public void setId(Long carreraId) {
        this.carreraId = carreraId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getDuracion() {
        return duracion;
    }

    public void setDuracion(Integer duracion) {
        this.duracion = duracion;
    }

    public List<EstudianteDeCarrera> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<EstudianteDeCarrera> estudiantes) {
        this.estudiantes = estudiantes;
    }
}
