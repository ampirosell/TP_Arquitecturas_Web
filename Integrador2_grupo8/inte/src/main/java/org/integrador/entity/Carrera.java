package org.integrador.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.List;

@Entity
public class Carrera {
    @Id
    private Long carreraId;
    List<EstudianteDeCarrera> estudiantes;

    public void setId(Long carreraId) {
        this.carreraId = carreraId;
    }

    public Long getId() {
        return carreraId;
    }
}
