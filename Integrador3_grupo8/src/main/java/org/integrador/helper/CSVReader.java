package org.integrador.helper;

import jakarta.transaction.Transactional;
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
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
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
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(archivo);
        if (inputStream == null) {
            throw new FileNotFoundException("No se encontró el archivo: " + archivo);
        }

        Reader in = new InputStreamReader(inputStream);
        CSVParser csvParser = CSVFormat.EXCEL
                .withFirstRecordAsHeader()
                .withIgnoreSurroundingSpaces()
                .parse(in);

        return csvParser.getRecords();
    }

    @Transactional
    public void populateDB() throws Exception {
        System.out.println("Iniciando el programa, cargando datos.");
        try {
            // Verificar si ya hay datos en la base de datos
            boolean hayCarreras = !cr.findAll().isEmpty();
            boolean hayEstudiantes = !er.findAll().isEmpty();
            boolean hayMatriculas = !ecr.findAll().isEmpty();


            // Cargar carreras solo si no existen
            if (!hayCarreras)
                insertCarreras();
            else
                System.out.println("Hay Carreras, no se creará ninguna.");


            // Cargar estudiantes solo si no existen

            if (!hayEstudiantes)
                insertEstudiantes();
            else
                System.out.println("Hay estudiantes, no se creará ninguno.");

            // Cargar matrículas solo si no existen
            if (!hayMatriculas)
                insertMatriculas();
            else
                System.out.println("Hay matriculaciones, no se creará ninguna.");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }


    private void insertEstudiantes() throws IOException {
        for(CSVRecord row : getData("estudiantes.csv")) {
            //DNI,nombre,apellido,edad,genero,ciudad,LU
            if(row.size() >= 7) {
                String dniString = row.get(0);
                String nombre = row.get(1);
                String apellido = row.get(2);
                String edadString = row.get(3);
                String genero = row.get(4);
                String ciudad = row.get(5);
                String nroLUString = row.get(6);
                if(!dniString.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !edadString.isEmpty() && !genero.isEmpty() && !nroLUString.isEmpty() && !ciudad.isEmpty()) {
                    try {
                        String nroLU = nroLUString;
                        Integer dni = Integer.parseInt(dniString);
                        Integer edad = Integer.parseInt(edadString);
                        Estudiante estudiante = new Estudiante();
                        estudiante.setDni(dni);
                        estudiante.setNombre(nombre);
                        estudiante.setApellido(apellido);
                        estudiante.setEdad(edad);
                        estudiante.setGenero(genero);
                        estudiante.setCiudadDeResidencia(ciudad);
                        estudiante.setNumeroLU(nroLU);
                        er.save(estudiante);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de estudiante: " + e.getMessage());
                    }
                }
            }
        }
    }
    private void insertCarreras() throws IOException {
        for(CSVRecord row : getData("carreras.csv")) {
            //nombre,duracion
            if(row.size() >= 2) {
                String idString = row.get(0);
                String nombre = row.get(1);
                String duracionString = row.get(2);
                if(!nombre.isEmpty() && !duracionString.isEmpty()) {
                    try {
                        Long id = Long.parseLong(idString);
                        Integer duracion = Integer.parseInt(duracionString);
                        Carrera carrera = new Carrera();
                        carrera.setId(id);
                        carrera.setNombre(nombre);
                        carrera.setDuracion(duracion);
                        cr.save(carrera);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de carrera: " + e.getMessage());
                    }
                }
            }
        }
    }
    private void insertMatriculas() throws IOException {
        for (CSVRecord row : getData("estudianteCarrera.csv")) {
            if (row.size() >= 3) { // dni,carrera_id,anio_inicio
                try {
                    Long id = Long.parseLong(row.get(0));
                    Integer dni = Integer.parseInt(row.get(1));
                    Long idCarrera = Long.parseLong(row.get(2));
                    Integer anioInicio = Integer.parseInt(row.get(3));
                    Integer anioFin = Integer.parseInt(row.get(4));
                    Integer antiguedad = Integer.parseInt(row.get(5));

                    // Convertir año a Date
                    Date fechaInscripcion = new GregorianCalendar(anioInicio, 0, 1).getTime();
                    Date fechaFinalizacion  = new GregorianCalendar(anioFin, 0, 1).getTime();


                    Estudiante estudiante = er.findByDni(dni).orElse(null);
                    Carrera carrera = cr.findById(idCarrera).orElse(null);

                    if (estudiante != null && carrera != null) {
                        // Crear matrícula
                        EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, fechaInscripcion, fechaFinalizacion, antiguedad);
                        ecr.save(matricula);
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de datos: " + e.getMessage());
                }
            }
        }
    }

}