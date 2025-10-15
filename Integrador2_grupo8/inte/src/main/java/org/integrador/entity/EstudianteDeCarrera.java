package org.integrador.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "estudiante_carrera")
public class EstudianteDeCarrera {
    
    @EmbeddedId
    private EstudianteCarreraId id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("estudianteId")
    @JoinColumn(name = "estudiante_id", nullable = false)
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

    // Constructores
    public EstudianteDeCarrera() {}

    public EstudianteDeCarrera(Estudiante estudiante, Carrera carrera, Date fechaInscripcion) {
        this.id = new EstudianteCarreraId(estudiante.getId(), carrera.getCarreraId());
        this.estudiante = estudiante;
        this.carrera = carrera;
        this.fechaInscripcion = fechaInscripcion;
        this.graduado = false;
    }

    // Getters y Setters
    public EstudianteCarreraId getId() {
        return id;
    }

    public void setId(EstudianteCarreraId id) {
        this.id = id;
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

    // Método para calcular antigüedad
    public long getAntiguedadEnDias() {
        if (fechaInscripcion == null) {
            return 0;
        }
        Date fechaActual = graduado && fechaGraduacion != null ? fechaGraduacion : new Date();
        return (fechaActual.getTime() - fechaInscripcion.getTime()) / (1000 * 60 * 60 * 24);
    }

    public int getAntiguedadEnAnios() {
        return (int) (getAntiguedadEnDias() / 365);
    }
}
