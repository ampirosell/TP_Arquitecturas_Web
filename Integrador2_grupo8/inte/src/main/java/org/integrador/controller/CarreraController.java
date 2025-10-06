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
public class CarreraController {

    @Autowired
    private CarreraService carreraService;

    @Autowired
    private EstudianteService estudianteService;

    // b) Matricular un estudiante en una carrera
    @PostMapping("/{carreraId}/matricular/{estudianteId}")
    public ResponseEntity<?> matricularEstudiante(@PathVariable Long carreraId, @PathVariable Long estudianteId, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInscripcion) {
        
        try {
            EstudianteDeCarrera inscripcion = carreraService.matricularEstudiante(estudianteId, carreraId, fechaInscripcion);
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
    public ResponseEntity<String> obtenerTodasLasCarreras() {
        /*List<Carrera> carreras = carreraService.obtenerTodasLasCarreras();
        return ResponseEntity.ok(carreras);*/
        return ResponseEntity.ok("Hola");
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

}
