package org.integrador.helper;

import org.integrador.entity.Carrera;
import org.integrador.entity.Estudiante;
import org.integrador.entity.EstudianteDeCarrera;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.integrador.repository.CarreraRepository;
import org.integrador.repository.EstudianteRepository;
import org.integrador.repository.EstudianteDeCarreraRepository;

import jakarta.persistence.EntityManager;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class CSVReader {
    private EntityManager em;
    private CarreraRepository cr;
    private EstudianteRepository er;
    private EstudianteDeCarreraRepository ecr;
    public CSVReader(EntityManager em, CarreraRepository cr, EstudianteDeCarreraRepository ecr, EstudianteRepository er){
        this.em = em;
        this.cr = cr;
        this.er = er;
        this.ecr = ecr;
    }

    private Iterable<CSVRecord> getData(String archivo) throws IOException {
        String path = "inte/src/main/resources/" + archivo;
        Reader in = new FileReader(path);

        CSVParser csvParser = CSVFormat.EXCEL
                .withFirstRecordAsHeader() // usa la primera fila como encabezado
                .withIgnoreSurroundingSpaces()
                .parse(in);

        return csvParser.getRecords();
    }

    public void populateDB() throws Exception {
        try {
            // Verificar si ya hay datos en la base de datos
            boolean hayCarreras = cr.findAll().size() > 0;
            boolean hayEstudiantes = er.findAll().size() > 0;
            boolean hayMatriculas = ecr.findAll().size() > 0;
            

            // Cargar carreras solo si no existen
            if (!hayCarreras) {
                em.getTransaction().begin();
                insertCarreras();
                em.getTransaction().commit();
            }

            // Cargar estudiantes solo si no existen
            
            if (!hayEstudiantes) {
                em.getTransaction().begin();
                insertEstudiantes();
                em.getTransaction().commit();
            }

            // Cargar matrículas solo si no existen
            if (!hayMatriculas) {
                em.getTransaction().begin();
                insertMatriculas();
                em.getTransaction().commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    private void insertEstudiantes() throws IOException {
        for (CSVRecord row : getData("estudiantes.csv")) {
            // DNI,nombre,apellido,edad,genero,ciudad,LU
            if (row.size() >= 7) {
                String dniString = row.get(0);
                String nombre = row.get(1);
                String apellido = row.get(2);
                String edadString = row.get(3);
                String genero = row.get(4);
                String ciudad = row.get(5);
                String nroLUString = row.get(6);

                if (!dniString.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty()
                        && !edadString.isEmpty() && !genero.isEmpty()
                        && !nroLUString.isEmpty() && !ciudad.isEmpty()) {
                    try {
                        int dni = Integer.parseInt(dniString);
                        int edad = Integer.parseInt(edadString);
                        String nroLU = nroLUString;

                        Estudiante estudiante = new Estudiante(dni, nombre, apellido, edad, genero, nroLU, ciudad);
                        er.create(estudiante);

                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de estudiante: " + e.getMessage());
                    }
                }
            }
        }
    }
    private void insertCarreras() throws IOException {
        for(CSVRecord row : getData("carreras.csv")) {
            //id_carrera,nombre, duracion
            if(row.size() >= 3) {
                String id_carreraString = row.get(0);
                String nombre = row.get(1);
                String duracionString = row.get(2);
                if(!id_carreraString.isEmpty() && !nombre.isEmpty() && !duracionString.isEmpty()) {
                    try {
                        Long id_carrera = Long.parseLong(id_carreraString);
                        int duracion = Integer.parseInt(duracionString);
                        Carrera carrera = new Carrera(id_carrera, nombre, duracion);
                        cr.create(carrera);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de dirección: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void insertMatriculas() throws IOException {
        for (CSVRecord row : getData("estudianteCarrera.csv")) {
            if (row.size() >= 6) { // id,id_estudiante,id_carrera,inscripcion,graduacion,antiguedad
                try {
                    int dni = Integer.parseInt(row.get("id_estudiante"));
                    int idCarrera = Integer.parseInt(row.get("id_carrera"));
                    int anioInicio = Integer.parseInt(row.get("inscripcion"));
                    int anioGraduacion = Integer.parseInt(row.get("graduacion"));

                    // Convertir año a Date
                    Date fechaInscripcion = new GregorianCalendar(anioInicio, 0, 1).getTime();
                    Date fechaGraduacion = null;
                    if (anioGraduacion > 0) {
                        fechaGraduacion = new GregorianCalendar(anioGraduacion, 0, 1).getTime();
                    }

                    Estudiante estudiante = er.findById(dni);
                    Carrera carrera = cr.findById(idCarrera);

                    if (estudiante != null && carrera != null) {
                        // Verificar si la matrícula ya existe usando una consulta
                        List<EstudianteDeCarrera> matriculasExistentes = em.createQuery(
                            "SELECT ec FROM EstudianteDeCarrera ec WHERE ec.estudiante.dni = :dni AND ec.carrera.id = :carreraId", 
                            EstudianteDeCarrera.class)
                            .setParameter("dni", dni)
                            .setParameter("carreraId", (long)idCarrera)
                            .getResultList();
                        
                        if (matriculasExistentes.isEmpty()) {
                            // Crear matrícula
                            EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, fechaInscripcion);
                            if (fechaGraduacion != null) {
                                matricula.setFechaGraduacion(fechaGraduacion);
                                matricula.setGraduado(true);
                            }
                            ecr.create(matricula);
                        }
                    } else {
                        System.err.println("No se pudo crear matrícula: estudiante=" + (estudiante != null ? "OK" : "NULL") + 
                                         ", carrera=" + (carrera != null ? "OK" : "NULL") + 
                                         " para DNI=" + dni + ", carrera=" + idCarrera);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de datos: " + e.getMessage());
                }
            }
        }
    }

}