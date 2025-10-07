package org.integrador.service;


import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;

import org.integrador.repository.EstudianteDeCarreraRepository;

import javax.persistence.EntityManager;
import java.util.Date;

public class EstudianteCarreraService {

    private EntityManager em;
    private EstudianteDeCarreraRepository ecr;

    public EstudianteCarreraService(EntityManager em){
        this.em = em;
        this.ecr = new EstudianteDeCarreraRepository(em);
    }

    // b) Matricular estudiante en una carrera
    public void matricularEstudiante(Estudiante estudiante, Carrera carrera, Date anioInscripcion){
        // Verificar si ya existe la matricula
        if(ecr.existe(estudiante.getId(), carrera.getId())){
            throw new RuntimeException("El estudiante ya está inscripto en esta carrera");
        }

        EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, anioInscripcion);

        em.getTransaction().begin();
        ecr.create(matricula);
        em.getTransaction().commit();
    }
}

