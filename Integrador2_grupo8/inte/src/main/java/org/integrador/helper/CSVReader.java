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
        String path = "integrador2/src/main/resources/" + archivo;
        Reader in = new FileReader(path);

        CSVParser csvParser = CSVFormat.EXCEL
                .withFirstRecordAsHeader() // usa la primera fila como encabezado
                .withIgnoreSurroundingSpaces()
                .parse(in);

        return csvParser.getRecords();
    }

    public void populateDB() throws Exception {
        try {
            em.getTransaction().begin();
            insertEstudiantes();
            insertCarreras();
            insertMatriculas();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertEstudiantes() throws IOException {
        for(CSVRecord row : getData("estudiantes.csv")) {
            //DNI,nombre,apellido,edad,genero,ciudad,LU
            if(row.size() >= 7) {
                String estudianteIdString = row.get(0);
                String dniString = row.get(1);
                String nombre = row.get(2);
                String apellido = row.get(3);
                String edadString = row.get(4);
                String genero = row.get(5);
                String ciudad = row.get(6);
                String nroLUString = row.get(7);
                if(!dniString.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !edadString.isEmpty() && !genero.isEmpty() && !nroLUString.isEmpty() && !ciudad.isEmpty()) {
                    try {
                        String nroLU = nroLUString;
                        Long estudianteId = Long.parseLong(estudianteIdString);
                        int edad = Integer.parseInt(edadString);
                        String dni = dniString;
                        Estudiante estudiante = new Estudiante(estudianteId, nombre, apellido, edad, genero,dni, nroLU, ciudad);
                        er.create(estudiante);
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de dirección: " + e.getMessage());
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
            if (row.size() >= 4) { // id_estudiante,id_carrera,anio_inicio
                try {
                    int dni = Integer.parseInt(row.get(1));
                    int idCarrera = Integer.parseInt(row.get(2));
                    int anioInicio = Integer.parseInt(row.get(3));

                    // Convertir año a Date
                    Date fechaInscripcion = new GregorianCalendar(anioInicio, 0, 1).getTime();

                    Estudiante estudiante = er.findById(dni);
                    Carrera carrera = cr.findById(idCarrera);

                    // Crear matrícula
                    EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, fechaInscripcion);

                    em.getTransaction().begin();
                    ecr.create(matricula);
                    em.getTransaction().commit();

                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de datos: " + e.getMessage());
                }
            }
        }
    }

}