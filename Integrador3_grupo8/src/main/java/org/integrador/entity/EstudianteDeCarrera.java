package org.integrador.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "estudiante_carrera")
public class EstudianteDeCarrera {
    
    @EmbeddedId
    private EstudianteCarreraId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dni")
    @JoinColumn(name = "dni", nullable = false)
    @JsonBackReference
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("carreraId")
    @JoinColumn(name = "carrera_id", nullable = false)
    private Carrera carrera;
    
    @Column(name = "fecha_inscripcion", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaInscripcion;
    
    @Column(name = "fecha_graduacion")
    @Temporal(TemporalType.DATE)
    private Date fechaGraduacion;
    
    @Column(name = "graduado", nullable = false)
    private boolean graduado;

    @Column(name="antiguedad")
    private Integer antiguedad;

    // Constructores
    public EstudianteDeCarrera() {}

    public EstudianteDeCarrera(Estudiante estudiante, Carrera carrera, Date fechaInscripcion, Date fechaGraduacion, Integer antiguedad) {
        this.id = new EstudianteCarreraId(estudiante.getDni(), carrera.getId());
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.fechaInscripcion = fechaInscripcion;
        this.fechaGraduacion = fechaGraduacion;
        this.antiguedad = antiguedad;
        this.graduado = false;
    }

    // Getters y Setters
    public EstudianteCarreraId getId() {
        return id;
    }


    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Carrera getCarrera() {
        return carrera;
    }

    public void setCarrera(Carrera carrera) {
        this.carrera = carrera;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public Date getFechaGraduacion() {
        return fechaGraduacion;
    }

    public void setFechaGraduacion(Date fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    public boolean isGraduado() {
        return graduado;
    }

    public void setGraduado(boolean graduado) {
        this.graduado = graduado;
    }
    public void setAntiguedad(Integer antiguedad) {
        this.antiguedad = antiguedad;
    }

    public int getAntiguedadEnAnios() {
        return (int) (this.antiguedad);
    }


}
