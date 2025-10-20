package org.integrador.service;

import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.entity.EstudianteCarreraId;
import org.integrador.repository.EstudianteDeCarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class EstudianteCarreraService {

    @Autowired
    private EstudianteDeCarreraRepository ecr;

    // b) Matricular estudiante en una carrera
    public EstudianteDeCarrera matricularEstudiante(Estudiante estudiante, Carrera carrera, Date anioInscripcion){
        // Verificar si ya existe la matricula
        EstudianteCarreraId id = new EstudianteCarreraId(estudiante.getDni(), carrera.getId());
        if(ecr.existsById(id)){
            throw new RuntimeException("El estudiante ya está inscripto en esta carrera");
        }

        EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, anioInscripcion, null, 0);
        return ecr.save(matricula);
    }
}

