package org.integrador.controller;

import org.integrador.entity.Estudiante;
import org.integrador.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/estudiantes")
@CrossOrigin(origins = "*")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    // a) Dar de alta un estudiante
    @PostMapping
    public ResponseEntity<?> crearEstudiante(@RequestBody Estudiante estudiante) {
        try {
            Estudiante nuevoEstudiante = estudianteService.crearEstudiante(estudiante);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoEstudiante);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // c) Recuperar todos los estudiantes ordenados
    @GetMapping
    public ResponseEntity<List<Estudiante>> obtenerTodosLosEstudiantes() {
        List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }

    // d) Recuperar estudiante por número de libreta universitaria
    @GetMapping("/libreta/{numeroLU}")
    public ResponseEntity<?> obtenerEstudiantePorNumeroLU(@PathVariable String numeroLU) {
        Optional<Estudiante> estudiante = estudianteService.obtenerEstudiantePorNumeroLU(numeroLU);
        if (estudiante.isPresent()) {
            return ResponseEntity.ok(estudiante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // e) Recuperar estudiantes por género
    @GetMapping("/genero/{genero}")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesPorGenero(@PathVariable String genero) {
        List<Estudiante> estudiantes = estudianteService.obtenerEstudiantesPorGenero(genero);
        return ResponseEntity.ok(estudiantes);
    }

    // g) Recuperar estudiantes de una carrera filtrados por ciudad
    @GetMapping("/carrera/{nombreCarrera}/ciudad/{ciudad}")
    public ResponseEntity<List<Estudiante>> obtenerEstudiantesPorCarreraYCiudad(
            @PathVariable String nombreCarrera, 
            @PathVariable String ciudad) {
        List<Estudiante> estudiantes = estudianteService.obtenerEstudiantesPorCarreraYCiudad(nombreCarrera, ciudad);
        return ResponseEntity.ok(estudiantes);
    }


    // Obtener estudiante por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerEstudiantePorId(@PathVariable Long id) {
        Optional<Estudiante> estudiante = estudianteService.obtenerEstudiantePorId(id);
        if (estudiante.isPresent()) {
            return ResponseEntity.ok(estudiante.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar estudiante
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarEstudiante(@PathVariable Long id) {
        try {
            estudianteService.eliminarEstudiante(id);
            return ResponseEntity.ok().body("Estudiante eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    // Verificar existencia por DNI
    @GetMapping("/existe/dni/{dni}")
    public ResponseEntity<Boolean> existeEstudiantePorDni(@PathVariable String dni) {
        boolean existe = estudianteService.existeEstudiantePorDni(dni);
        return ResponseEntity.ok(existe);
    }

}
