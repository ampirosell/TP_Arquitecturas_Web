package org.integrador.service;

import org.integrador.entity.Estudiante;
import org.integrador.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    // a) Dar de alta un estudiante
    public Estudiante crearEstudiante(Estudiante estudiante) {
        // Validacion
        if (estudianteRepository.existsByDni(estudiante.getDni())) {
            throw new RuntimeException("Ya existe un estudiante con el DNI: " + estudiante.getDni());
        }
        
        return estudianteRepository.save(estudiante);
    }

    // c) Recuperar todos los estudiantes ordenados
    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteRepository.findAllOrderedByApellidoAndNombre();
    }

    // d) Recuperar estudiante por número de libreta universitaria
    public Optional<Estudiante> obtenerEstudiantePorNumeroLU(String numeroLU) {
        return estudianteRepository.findByNumeroLU(numeroLU);
    }

    // e) Recuperar estudiantes por género
    public List<Estudiante> obtenerEstudiantesPorGenero(String genero) {
        return estudianteRepository.findByGenero(genero);
    }

    // g) Recuperar estudiantes de una carrera filtrados por ciudad
    public List<Estudiante> obtenerEstudiantesPorCarreraYCiudad(String nombreCarrera, String ciudad) {
        return estudianteRepository.findByCarreraAndCiudad(nombreCarrera, ciudad);
    }

    // Obtener estudiante por ID
    public Optional<Estudiante> obtenerEstudiantePorId(Long id) {
        return estudianteRepository.findById(id);
    }

    // Eliminar estudiante
    public void eliminarEstudiante(Long id) {
        estudianteRepository.deleteById(id);
    }

    // Verificar si existe estudiante por DNI
    public boolean existeEstudiantePorDni(String dni) {
        return estudianteRepository.existsByDni(dni);
    }

}
