package org.integrador.controller;

import org.integrador.DTO.CarreraConEstudiantesDTO;
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

    // f) Recuperar carreras con estudiantes inscriptos ordenadas por cantidad
    @GetMapping("/con-estudiantes")
    public ResponseEntity<List<CarreraConEstudiantesDTO>> obtenerCarrerasConEstudiantesOrdenadas() {
        List<CarreraConEstudiantesDTO> carreras = carreraService.obtenerCarrerasConEstudiantesOrdenadasPorInscripciones();
        return ResponseEntity.ok(carreras);
    }
}
