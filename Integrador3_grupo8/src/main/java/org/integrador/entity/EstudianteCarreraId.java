package org.integrador.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class EstudianteCarreraId implements Serializable {

    private Integer dni;
    private Long carreraId;

    // Constructores
    public EstudianteCarreraId() {}

    public EstudianteCarreraId(Integer dni, Long carreraId) {
        this.dni = dni;
        this.carreraId = carreraId;
    }

    // Getters y Setters
    public Integer getDni() {
        return dni;
    }

    public void setDni(Integer dni) {
        this.dni = dni;
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
        return Objects.equals(dni, that.dni) &&
               Objects.equals(carreraId, that.carreraId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni, carreraId);
    }

    @Override
    public String toString() {
        return "EstudianteCarreraId{" +
                "dni=" + dni +
                ", carreraId=" + carreraId +
                '}';
    }
}
