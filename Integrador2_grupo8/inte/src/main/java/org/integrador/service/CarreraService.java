package org.integrador.service;

import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.entity.EstudianteCarreraId;
import org.integrador.repository.CarreraRepository;
import org.integrador.repository.EstudianteDeCarreraRepository;
import org.integrador.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarreraService {

    @Autowired
    private CarreraRepository carreraRepository;

    @Autowired
    private EstudianteDeCarreraRepository estudianteDeCarreraRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    // b) Matricular un estudiante en una carrera
    public EstudianteDeCarrera matricularEstudiante(Long estudianteId, Long carreraId, Date fechaInscripcion) {
        // Verificar que el estudiante no esté ya inscripto en esta carrera
        EstudianteCarreraId id = new EstudianteCarreraId(estudianteId, carreraId);
        
        if (estudianteDeCarreraRepository.existsById(id)) {
            throw new RuntimeException("El estudiante ya está inscripto en esta carrera");
        }

        // Obtener las entidades completas
        Optional<Estudiante> estudianteOpt = estudianteRepository.findById(estudianteId);
        Optional<Carrera> carreraOpt = carreraRepository.findById(carreraId);
        
        if (estudianteOpt.isEmpty()) {
            throw new RuntimeException("Estudiante no encontrado");
        }
        if (carreraOpt.isEmpty()) {
            throw new RuntimeException("Carrera no encontrada");
        }

        EstudianteDeCarrera inscripcion = new EstudianteDeCarrera(
            estudianteOpt.get(), 
            carreraOpt.get(), 
            fechaInscripcion != null ? fechaInscripcion : new Date()
        );
        
        return estudianteDeCarreraRepository.save(inscripcion);
    }


    // f) Recuperar carreras con estudiantes inscriptos ordenadas por cantidad
    public List<Carrera> obtenerCarrerasConEstudiantesOrdenadasPorInscripciones() {
        return carreraRepository.findCarrerasWithStudentsOrderedByInscripciones();
    }


    // Crear nueva carrera
    public Carrera crearCarrera(Carrera carrera) {
        if (carreraRepository.existsByNombre(carrera.getNombre())) {
            throw new RuntimeException("Ya existe una carrera con el nombre: " + carrera.getNombre());
        }
        return carreraRepository.save(carrera);
    }

    // Obtener todas las carreras
    public List<Carrera> obtenerTodasLasCarreras() {
        return carreraRepository.findAll();
    }

    // Obtener carrera por ID
    public Optional<Carrera> obtenerCarreraPorId(Long id) {
        return carreraRepository.findById(id);
    }

    // Eliminar carrera
    public void eliminarCarrera(Long id) {
        carreraRepository.deleteById(id);
    }

    // Contar estudiantes por carrera
    public Long contarEstudiantesPorCarrera(Long carreraId) {
        return estudianteDeCarreraRepository.countByCarrera(carreraId);
    }

    // Contar graduados por carrera
    public Long contarGraduadosPorCarrera(Long carreraId) {
        return estudianteDeCarreraRepository.countGraduadosByCarrera(carreraId);
    }

    // Contar estudiantes activos por carrera
    public Long contarActivosPorCarrera(Long carreraId) {
        return estudianteDeCarreraRepository.countActivosByCarrera(carreraId);
    }
}
