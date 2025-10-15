package org.integrador.service;


import org.integrador.DTO.EstudianteDTO;
import org.integrador.entity.Estudiante;
import org.integrador.repository.EstudianteRepository;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class EstudianteService {

    private EntityManager em;
    private EstudianteRepository er;

    public EstudianteService(EntityManager em){
        this.em = em;
        this.er = new EstudianteRepository(em);
    }

    // a) Agregar estudiante
    public void agregarEstudiante(Estudiante estudiante){
        em.getTransaction().begin();
        er.create(estudiante);
        em.getTransaction().commit();
    }

    // c) Obtener todos los estudiantes
    public List<EstudianteDTO> obtenerEstudiantes(){
        List<Estudiante> estudiantes = er.findAll();
        return estudiantes.stream().map(EstudianteDTO::new).collect(Collectors.toList());
    }

    // d) Obtener estudiante por LU
    public EstudianteDTO obtenerEstudiantePorLU(String LU){
        return new EstudianteDTO(er.findByLU(LU));
    }

    // e) Obtener estudiantes por género
    public List<EstudianteDTO> obtenerEstudiantesPorGenero(String genero){
        List<Estudiante> estudiantes = er.findByGenero(genero);
        return estudiantes.stream().map(EstudianteDTO::new).collect(Collectors.toList());
    }

    // g) Obtener estudiantes por carrera y ciudad
    public List<EstudianteDTO> obtenerEstudiantesPorCarreraCiudad(int idCarrera, String ciudad){
        List<Estudiante> estudiantes = er.findByCarreraAndCiudad(idCarrera, ciudad);
        return estudiantes.stream().map(EstudianteDTO::new).collect(Collectors.toList());
    }
}
