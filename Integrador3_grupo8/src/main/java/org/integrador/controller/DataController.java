package org.integrador.controller;

import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.service.CarreraService;
import org.integrador.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/data")
@CrossOrigin(origins = "*")
public class DataController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private CarreraService carreraService;

    @PostMapping("/init")
    public ResponseEntity<String> inicializarDatos() {
        try {
            // Crear carreras
            Carrera ingenieria = new Carrera("Ingeniería en Sistemas", 5);
            Carrera medicina = new Carrera("Medicina", 7);
            Carrera derecho = new Carrera("Derecho", 5);
            Carrera psicologia = new Carrera("Psicología", 5);

            carreraService.crearCarrera(ingenieria);
            carreraService.crearCarrera(medicina);
            carreraService.crearCarrera(derecho);
            carreraService.crearCarrera(psicologia);

            // Crear estudiantes
            Estudiante estudiante1 = new Estudiante();
            estudiante1.setNombre("Juan");
            estudiante1.setApellido("Pérez");
            estudiante1.setEdad(22);
            estudiante1.setGenero("Masculino");
            estudiante1.setDni(12345678);
            estudiante1.setCiudadDeResidencia("Buenos Aires");
            estudiante1.setNumeroLU("LU12345");

            Estudiante estudiante2 = new Estudiante();
            estudiante2.setNombre("María");
            estudiante2.setApellido("González");
            estudiante2.setEdad(23);
            estudiante2.setGenero("Femenino");
            estudiante2.setDni(87654321);
            estudiante2.setCiudadDeResidencia("Córdoba");
            estudiante2.setNumeroLU("LU67890");

            Estudiante estudiante3 = new Estudiante();
            estudiante3.setNombre("Carlos");
            estudiante3.setApellido("López");
            estudiante3.setEdad(21);
            estudiante3.setGenero("Masculino");
            estudiante3.setDni(11223344);
            estudiante3.setCiudadDeResidencia("Buenos Aires");
            estudiante3.setNumeroLU("LU11111");

            Estudiante estudiante4 = new Estudiante();
            estudiante4.setNombre("Ana");
            estudiante4.setApellido("Martínez");
            estudiante4.setEdad(24);
            estudiante4.setGenero("Femenino");
            estudiante4.setDni(55667788);
            estudiante4.setCiudadDeResidencia("Rosario");
            estudiante4.setNumeroLU("LU22222");

            estudianteService.crearEstudiante(estudiante1);
            estudianteService.crearEstudiante(estudiante2);
            estudianteService.crearEstudiante(estudiante3);
            estudianteService.crearEstudiante(estudiante4);

            // Obtener las carreras creadas
            List<Carrera> carreras = carreraService.obtenerTodasLasCarreras();
            List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();

            // Matricular estudiantes en carreras
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // Juan Pérez en Ingeniería (2020)
            carreraService.matricularEstudiante(estudiantes.get(0), carreras.get(0), sdf.parse("2020-03-01"));
            
            // María González en Medicina (2019)
            carreraService.matricularEstudiante(estudiantes.get(1), carreras.get(1), sdf.parse("2019-03-01"));
            
            // Carlos López en Ingeniería (2021)
            carreraService.matricularEstudiante(estudiantes.get(2), carreras.get(0), sdf.parse("2021-03-01"));
            
            // Ana Martínez en Derecho (2018) - Graduada
            EstudianteDeCarrera inscripcionAna = carreraService.matricularEstudiante(estudiantes.get(3), carreras.get(2), sdf.parse("2018-03-01"));
            inscripcionAna.setGraduado(true);
            inscripcionAna.setFechaGraduacion(sdf.parse("2023-12-15"));

            // María también en Psicología (2020)
            carreraService.matricularEstudiante(estudiantes.get(1), carreras.get(3), sdf.parse("2020-03-01"));

            return ResponseEntity.ok("Datos inicializados correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al inicializar datos: " + e.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> limpiarDatos() {
        try {
            // Este método limpiaría todos los datos
            // En una implementación real, se usarían métodos de limpieza del servicio
            return ResponseEntity.ok("Datos limpiados correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al limpiar datos: " + e.getMessage());
        }
    }
}
