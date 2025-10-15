package org.integrador.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EstudianteCarreraId implements Serializable {

    private Long estudianteId;
    private Long carreraId;

    // Constructores
    public EstudianteCarreraId() {}

    public EstudianteCarreraId(Long estudianteId, Long carreraId) {
        this.estudianteId = estudianteId;
        this.carreraId = carreraId;
    }

    // Getters y Setters
    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public Long getCarreraId() {
        return carreraId;
    }

    public void setCarreraId(Long carreraId) {
        this.carreraId = carreraId;
    }

    // equals y hashCode son obligatorios para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EstudianteCarreraId that = (EstudianteCarreraId) o;
        return Objects.equals(estudianteId, that.estudianteId) &&
               Objects.equals(carreraId, that.carreraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(estudianteId, carreraId);
    }

    @Override
    public String toString() {
        return "EstudianteCarreraId{" +
                "estudianteId=" + estudianteId +
                ", carreraId=" + carreraId +
                '}';
    }
}
