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
            emf = Persistence.createEntityManagerFactory("Example");
        }

    public static void main(String[] args) {
        System.out.println("=== INTEGRADOR 2 - SISTEMA DE ESTUDIANTES ===");
        System.out.println("Iniciando aplicación JPA...\n");

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
            System.out.println("1. Cargando datos desde archivos CSV...");
            CSVReader csvReader = new CSVReader(em, cr, ecr, er);
            csvReader.populateDB();
            System.out.println("✓ Datos cargados exitosamente\n");

            // Los datos ya están cargados desde CSV, ahora ejecutamos las consultas requeridas

            // c) Obtener todos los estudiantes
            System.out.println("2. Listando todos los estudiantes:");
            List<EstudianteDTO> estudiantes = estudianteService.obtenerEstudiantes();
            for (EstudianteDTO est : estudiantes) {
                System.out.println("  - " + est.toString());
            }
            System.out.println("Total estudiantes: " + estudiantes.size() + "\n");

            // d) Obtener estudiante por LU
            System.out.println("3. Buscando estudiante por número de libreta:");
            if (!estudiantes.isEmpty()) {
                String luBuscar = estudiantes.get(0).getLU();
                EstudianteDTO estudianteEncontrado = estudianteService.obtenerEstudiantePorLU(luBuscar);
                System.out.println("  - Encontrado: " + estudianteEncontrado.toString() + "\n");
            }

            // e) Obtener estudiantes por género
            System.out.println("4. Estudiantes por género:");
            List<EstudianteDTO> estudiantesMasculinos = estudianteService.obtenerEstudiantesPorGenero("M");
            System.out.println("  Estudiantes masculinos: " + estudiantesMasculinos.size());
            List<EstudianteDTO> estudiantesFemeninos = estudianteService.obtenerEstudiantesPorGenero("F");
            System.out.println("  Estudiantes femeninos: " + estudiantesFemeninos.size() + "\n");

            // f) Obtener carreras con inscriptos
            System.out.println("5. Carreras con estudiantes inscriptos:");
            List<CarreraDTO> carrerasConInscriptos = carreraService.obtenerCarrerasConInscriptos();
            for (CarreraDTO carrera : carrerasConInscriptos) {
                System.out.println("  - " + carrera.toString());
            }
            System.out.println();

            // g) Obtener estudiantes por carrera y ciudad
            System.out.println("6. Estudiantes por carrera y ciudad:");
            List<Carrera> carreras = carreraService.obtenerTodasLasCarreras();
            if (!carreras.isEmpty() && !estudiantes.isEmpty()) {
                String ciudadBuscar = estudiantes.get(0).toString().contains("Buenos Aires") ? "Buenos Aires" : "Córdoba";
                List<EstudianteDTO> estudiantesPorCarreraCiudad = estudianteService.obtenerEstudiantesPorCarreraCiudad(
                    carreras.get(0).getId().intValue(), ciudadBuscar);
                System.out.println("  Estudiantes de " + carreras.get(0).getNombre() + " en " + ciudadBuscar + ": " + estudiantesPorCarreraCiudad.size());
            }
            System.out.println();

            // 3) Generar reporte
            System.out.println("7. Generando reporte de carreras:");
            List<ReporteDTO> reporte = carreraService.generarReporte();
            for (ReporteDTO r : reporte) {
                System.out.println("  - " + r.toString());
            }

            System.out.println("\n=== APLICACIÓN COMPLETADA EXITOSAMENTE ===");

        } catch (Exception e) {
            System.err.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
