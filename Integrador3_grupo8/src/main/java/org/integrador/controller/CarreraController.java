package org.integrador.controller;

import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.service.CarreraService;
import org.integrador.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/carreras")
@CrossOrigin(origins = "*")
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private EstudianteService estudianteService;

    // b) Matricular un estudiante en una carrera
    @PostMapping("/{carreraId}/matricular/{dni}")
    public ResponseEntity<?> matricularEstudiante(
            @PathVariable Long carreraId,
            @PathVariable Integer dni,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInscripcion) {
        
        try {
            EstudianteDeCarrera inscripcion = carreraService.matricularEstudiante(dni, carreraId, fechaInscripcion);
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Endpoint alternativo para matricular con objetos completos
    @PostMapping("/matricular")
    public ResponseEntity<?> matricularEstudianteCompleto(@RequestBody MatriculacionRequest request) {
        try {
            Optional<Estudiante> estudianteOpt = estudianteService.obtenerEstudiantePorDni(request.getDni());
            Optional<Carrera> carreraOpt = carreraService.obtenerCarreraPorId(request.getCarreraId());
            
            if (estudianteOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Estudiante no encontrado");
            }
            if (carreraOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Carrera no encontrada");
            }
            
            EstudianteDeCarrera inscripcion = carreraService.matricularEstudiante(
                estudianteOpt.get(), 
                carreraOpt.get(), 
                request.getFechaInscripcion()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // f) Recuperar carreras con estudiantes inscriptos ordenadas por cantidad
    @GetMapping("/con-estudiantes")
    public ResponseEntity<List<Carrera>> obtenerCarrerasConEstudiantesOrdenadas() {
        List<Carrera> carreras = carreraService.obtenerCarrerasConEstudiantesOrdenadasPorInscripciones();
        return ResponseEntity.ok(carreras);
    }

    // Obtener carreras con conteo de estudiantes
    @GetMapping("/con-conteo")
    public ResponseEntity<List<Object[]>> obtenerCarrerasConConteoEstudiantes() {
        List<Object[]> carreras = carreraService.obtenerCarrerasConConteoEstudiantes();
        return ResponseEntity.ok(carreras);
    }

    // Crear nueva carrera
    @PostMapping
    public ResponseEntity<?> crearCarrera(@RequestBody Carrera carrera) {
        try {
            Carrera nuevaCarrera = carreraService.crearCarrera(carrera);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCarrera);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Obtener todas las carreras
    @GetMapping
    public ResponseEntity<List<Carrera>> obtenerTodasLasCarreras() {
        List<Carrera> carreras = carreraService.obtenerTodasLasCarreras();
        return ResponseEntity.ok(carreras);
    }

    // Obtener carrera por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerCarreraPorId(@PathVariable Long id) {
        Optional<Carrera> carrera = carreraService.obtenerCarreraPorId(id);
        if (carrera.isPresent()) {
            return ResponseEntity.ok(carrera.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener carrera por nombre
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<?> obtenerCarreraPorNombre(@PathVariable String nombre) {
        Optional<Carrera> carrera = carreraService.obtenerCarreraPorNombre(nombre);
        if (carrera.isPresent()) {
            return ResponseEntity.ok(carrera.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar carrera
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCarrera(@PathVariable Long id, @RequestBody Carrera carrera) {
        try {
            carrera.setId(id);
            Carrera carreraActualizada = carreraService.actualizarCarrera(carrera);
            return ResponseEntity.ok(carreraActualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Eliminar carrera
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCarrera(@PathVariable Long id) {
        try {
            carreraService.eliminarCarrera(id);
            return ResponseEntity.ok().body("Carrera eliminada correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Estadísticas de carrera
    @GetMapping("/{id}/estadisticas")
    public ResponseEntity<Map<String, Long>> obtenerEstadisticasCarrera(@PathVariable Long id) {
        Map<String, Long> estadisticas = Map.of(
            "totalEstudiantes", carreraService.contarEstudiantesPorCarrera(id),
            "graduados", carreraService.contarGraduadosPorCarrera(id),
            "activos", carreraService.contarActivosPorCarrera(id)
        );
        return ResponseEntity.ok(estadisticas);
    }

    // Clase interna para el request de matriculación
    public static class MatriculacionRequest {
        private Integer dni;
        private Long carreraId;
        private Date fechaInscripcion;

        // Getters y Setters
        public Integer getDni() { return dni; }
        public void setDni(Integer dni) { this.dni = dni; }
        
        public Long getCarreraId() { return carreraId; }
        public void setCarreraId(Long carreraId) { this.carreraId = carreraId; }
        
        public Date getFechaInscripcion() { return fechaInscripcion; }
        public void setFechaInscripcion(Date fechaInscripcion) { this.fechaInscripcion = fechaInscripcion; }
    }
}
