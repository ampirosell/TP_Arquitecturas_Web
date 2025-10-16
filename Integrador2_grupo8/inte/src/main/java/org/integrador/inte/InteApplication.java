package org.integrador.inte;

import org.integrador.DTO.CarreraDTO;
import org.integrador.DTO.EstudianteDTO;
import org.integrador.DTO.ReporteDTO;
import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.helper.CSVReader;
import org.integrador.repository.CarreraRepository;
import org.integrador.repository.EstudianteDeCarreraRepository;
import org.integrador.repository.EstudianteRepository;
import org.integrador.service.CarreraService;
import org.integrador.service.EstudianteCarreraService;
import org.integrador.service.EstudianteService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Date;
import java.util.List;

public class InteApplication {

        private static final EntityManagerFactory emf;

        static {
            emf = Persistence.createEntityManagerFactory("integrador2");
        }

    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE ESTUDIANTES ===\n");

        EntityManager em = emf.createEntityManager();

        try {
            // Inicializar servicios
            EstudianteService estudianteService = new EstudianteService(em);
            CarreraService carreraService = new CarreraService(em);
            EstudianteCarreraService estudianteCarreraService = new EstudianteCarreraService(em);

            // Inicializar repositorios para CSVReader
            EstudianteRepository er = new EstudianteRepository(em);
            CarreraRepository cr = new CarreraRepository(em);
            EstudianteDeCarreraRepository ecr = new EstudianteDeCarreraRepository(em);

            // Cargar datos desde CSV
            CSVReader csvReader = new CSVReader(em, cr, ecr, er);
            csvReader.populateDB();

            // Los datos ya están cargados desde CSV, ahora ejecutamos las consultas requeridas

            // c) Obtener todos los estudiantes
            System.out.println("c) Todos los estudiantes:");
            List<EstudianteDTO> estudiantes = estudianteService.obtenerEstudiantes();
            for (EstudianteDTO est : estudiantes) {
                System.out.println("  - " + est.toString());
            }
            System.out.println("Total: " + estudiantes.size() + "\n");

            // d) Obtener estudiante por LU
            System.out.println("d) Estudiante por libreta universitaria:");
            if (!estudiantes.isEmpty()) {
                String luBuscar = estudiantes.get(0).getLU();
                EstudianteDTO estudianteEncontrado = estudianteService.obtenerEstudiantePorLU(luBuscar);
                System.out.println("  - " + estudianteEncontrado.toString() + "\n");
            }

            // e) Obtener estudiantes por género
            System.out.println("e) Estudiantes por género:");
            List<EstudianteDTO> estudiantesMasculinos = estudianteService.obtenerEstudiantesPorGenero("Male");
            System.out.println("  Masculinos: " + estudiantesMasculinos.size());
            List<EstudianteDTO> estudiantesFemeninos = estudianteService.obtenerEstudiantesPorGenero("Female");
            System.out.println("  Femeninos: " + estudiantesFemeninos.size() + "\n");

            // f) Obtener carreras con inscriptos
            System.out.println("f) Carreras con estudiantes inscriptos:");
            List<CarreraDTO> carrerasConInscriptos = carreraService.obtenerCarrerasConInscriptos();
            for (CarreraDTO carrera : carrerasConInscriptos) {
                System.out.println("  - " + carrera.toString());
            }
            System.out.println();

            // g) Obtener estudiantes por carrera y ciudad
            System.out.println("g) Estudiantes por carrera y ciudad:");
            List<Carrera> carreras = carreraService.obtenerTodasLasCarreras();
            if (!carreras.isEmpty() && !estudiantes.isEmpty()) {
                String ciudadBuscar = estudiantes.get(0).toString().contains("Buenos Aires") ? "Buenos Aires" : "Córdoba";
                List<EstudianteDTO> estudiantesPorCarreraCiudad = estudianteService.obtenerEstudiantesPorCarreraCiudad(
                    carreras.get(0).getId().intValue(), ciudadBuscar);
                System.out.println("  " + carreras.get(0).getNombre() + " en " + ciudadBuscar + ": " + estudiantesPorCarreraCiudad.size());
            }
            System.out.println();

            // 3) Generar reporte
            System.out.println("3) Reporte de carreras:");
            List<ReporteDTO> reporte = carreraService.generarReporte();
            for (ReporteDTO r : reporte) {
                System.out.println("  - " + r.toString());
            }

        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
