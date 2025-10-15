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
        String path = "inte/src/main/resources/" + archivo; // Cambiado de "inte/src/main/resources/" a "src/main/resources/"
        System.out.println("Intentando leer archivo: " + path);
        
        // Verificar si el archivo existe
        java.io.File file = new java.io.File(path);
        if (!file.exists()) {
            System.err.println("Archivo no encontrado: " + path);
            System.err.println("Directorio actual: " + System.getProperty("user.dir"));
            System.err.println("Archivos en src/main/resources/:");
            java.io.File resourcesDir = new java.io.File("src/main/resources/");
            if (resourcesDir.exists()) {
                String[] files = resourcesDir.list();
                if (files != null) {
                    for (String fileName : files) {
                        System.err.println("  - " + fileName);
                    }
                }
            }
            throw new IOException("Archivo no encontrado: " + path);
        }
        
        Reader in = new FileReader(path);
        System.out.println("Archivo encontrado, procesando...");

        CSVParser csvParser = CSVFormat.EXCEL
                .withFirstRecordAsHeader() // usa la primera fila como encabezado
                .withIgnoreSurroundingSpaces()
                .parse(in);

        return csvParser.getRecords();
    }

    public void populateDB() throws Exception {
        try {
            em.getTransaction().begin();

            // Verificar si faltan datos
            boolean carrerasVacias = cr.findAll().isEmpty();
            boolean estudiantesVacios = er.findAll().isEmpty();
            
            if (carrerasVacias || estudiantesVacios) {
                System.out.println("Cargando datos faltantes...");
                if (carrerasVacias) {
                    System.out.println("Insertando carreras...");
                    insertCarreras();
                }
                if (estudiantesVacios) {
                    System.out.println("Insertando estudiantes...");
                    insertEstudiantes();
                }
                System.out.println("Insertando matrículas...");
                insertMatriculas();
            } else {
                System.out.println("Hay datos ya cargados...");
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }
    
    private boolean isDatabaseEmpty() {
        try {
            // Verificar si hay carreras Y estudiantes en la base de datos
            boolean carrerasVacias = cr.findAll().isEmpty();
            boolean estudiantesVacios = er.findAll().isEmpty();
            return carrerasVacias && estudiantesVacios;
        } catch (Exception e) {
            return true; // Si hay error, asumir que está vacía
        }
    }

    private void insertEstudiantes() throws IOException {
        System.out.println("Iniciando inserción de estudiantes...");
        int contador = 0;
        int totalFilas = 0;
        
        for(CSVRecord row : getData("estudiantes.csv")) {
            totalFilas++;

            try {
                // Usar los nombres de las columnas del header
                String dniString = row.get("DNI");
                String nombre = row.get("nombre");
                String apellido = row.get("apellido");
                String edadString = row.get("edad");
                String genero = row.get("genero");
                String ciudad = row.get("ciudad");
                String nroLUString = row.get("LU");
                
                if(!dniString.isEmpty() && !nombre.isEmpty() && !apellido.isEmpty() && !edadString.isEmpty() && !genero.isEmpty() && !nroLUString.isEmpty() && !ciudad.isEmpty()) {
                    try {
                        String nroLU = nroLUString;
                        int edad = Integer.parseInt(edadString);
                        String dni = dniString;

                        // Crear el estudiante directamente
                        Estudiante estudiante = new Estudiante(nombre, apellido, edad, genero, dni, ciudad, nroLU);
                        er.create(estudiante);
                        contador++;
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de estudiante: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error al crear estudiante '" + nombre + " " + apellido + "': " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error procesando fila " + totalFilas + ": " + e.getMessage());
            }
        }
        System.out.println("Inserción de estudiantes completada. Total filas procesadas: " + totalFilas + ", Total insertados: " + contador);
    }
    private void insertCarreras() throws IOException {
        System.out.println("Iniciando inserción de carreras...");
        int contador = 0;
        int totalFilas = 0;
        
        for(CSVRecord row : getData("carreras.csv")) {
            totalFilas++;

            try {
                // Usar los nombres de las columnas del header
                String nombre = row.get("carrera");
                String duracionString = row.get("duracion");
                
                if(!nombre.isEmpty() && !duracionString.isEmpty()) {
                    try {
                        int duracion = Integer.parseInt(duracionString);
                        Carrera carrera = new Carrera(nombre, duracion);
                        cr.create(carrera);
                        contador++;
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato en datos de carrera: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error al crear carrera '" + nombre + "': " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Error procesando fila carrera " + totalFilas + ": " + e.getMessage());
            }
        }
        System.out.println("Inserción de carreras completada. Total filas procesadas: " + totalFilas + ", Total insertadas: " + contador);
    }
    private void insertMatriculas() throws IOException {
        System.out.println("Iniciando inserción de matrículas...");
        int contador = 0;
        int totalFilas = 0;
        
        // Crear mapas para mapear los IDs originales a los nuevos IDs generados
        java.util.Map<String, Long> dniToEstudianteId = new java.util.HashMap<>();
        java.util.Map<String, Long> nombreCarreraToCarreraId = new java.util.HashMap<>();
        java.util.Set<String> matriculasExistentes = new java.util.HashSet<>();

        // Mapear estudiantes por DNI - obtener todos los estudiantes de la base de datos
        try {
            java.util.List<Estudiante> estudiantes = er.findAll();
            for(Estudiante estudiante : estudiantes) {
                dniToEstudianteId.put(estudiante.getDni(), estudiante.getId());
            }
            System.out.println("Mapeados " + estudiantes.size() + " estudiantes para matrículas");
        } catch (Exception e) {
            System.err.println("Error al obtener estudiantes: " + e.getMessage());
        }

        // Mapear carreras por nombre
        for(CSVRecord row : getData("carreras.csv")) {
            if(row.size() >= 3) {
                String nombre = row.get("carrera");
                if(!nombre.isEmpty()) {
                    try {
                        Carrera carrera = cr.findByNombre(nombre);
                        nombreCarreraToCarreraId.put(nombre, carrera.getCarreraId());
                    } catch (Exception e) {
                        System.err.println("No se encontró carrera con nombre: " + nombre);
                    }
                }
            }
        }

        // Crear las matrículas
        for (CSVRecord row : getData("estudianteCarrera.csv")) {
            totalFilas++;
            try {
                String dni = row.get("id_estudiante");
                String nombreCarrera = getCarreraNameById(row.get("id_carrera"));
                int anioInicio = Integer.parseInt(row.get("inscripcion"));
                int anioGraduacion = Integer.parseInt(row.get("graduacion"));

                    // Convertir año a Date
                    Date fechaInscripcion = new GregorianCalendar(anioInicio, 0, 1).getTime();
                    Date fechaGraduacion = anioGraduacion > 0 ? new GregorianCalendar(anioGraduacion, 0, 1).getTime() : null;

                    Long estudianteId = dniToEstudianteId.get(dni);
                    Long carreraId = nombreCarreraToCarreraId.get(nombreCarrera);

                    if (estudianteId != null && carreraId != null) {
                        // Crear clave única para evitar duplicados
                        String claveMatricula = estudianteId + "-" + carreraId;
                        
                        if (matriculasExistentes.contains(claveMatricula)) {
                            continue;
                        }
                        
                        Estudiante estudiante = er.findById(estudianteId);
                        Carrera carrera = cr.findById(carreraId);

                        // Crear matrícula
                        EstudianteDeCarrera matricula = new EstudianteDeCarrera(estudiante, carrera, fechaInscripcion);
                        if (fechaGraduacion != null) {
                            matricula.setFechaGraduacion(fechaGraduacion);
                            matricula.setGraduado(true);
                        }

                        ecr.create(matricula);
                        matriculasExistentes.add(claveMatricula);
                        contador++;
                    } else {
                        System.err.println("No se encontró estudiante o carrera: DNI=" + dni + " (estudianteId=" + estudianteId + "), carrera=" + nombreCarrera + " (carreraId=" + carreraId + ")");
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Error en formato de datos en fila " + totalFilas + ": " + e.getMessage());
                } catch (Exception e) {
                    System.err.println("Error procesando matrícula en fila " + totalFilas + ": " + e.getMessage());
                }
            }
        }
        System.out.println("Inserción de matrículas completada. Total filas procesadas: " + totalFilas + ", Total insertadas: " + contador);
    }
    
    private String getCarreraNameById(String idCarrera) {
        // Mapear ID de carrera a nombre
        java.util.Map<String, String> idToNombre = new java.util.HashMap<>();
        idToNombre.put("1", "TUDAI");
        idToNombre.put("2", "Abogacia");
        idToNombre.put("3", "Ingenieria de Sistemas");
        idToNombre.put("4", "Ingenieria Electronica");
        idToNombre.put("5", "TUARI");
        idToNombre.put("6", "Veterinaria");
        idToNombre.put("7", "Profesorado Matematicas");
        idToNombre.put("8", "Medicina");
        idToNombre.put("9", "Educacion Fisica");
        idToNombre.put("10", "Licenciatura en Fisica");
        idToNombre.put("11", "Ingenieria Industrial");
        idToNombre.put("12", "Psicologia");
        idToNombre.put("13", "Periodismo");
        idToNombre.put("14", "Ciencias Economicas");
        idToNombre.put("15", "Arte");
        
        return idToNombre.get(idCarrera);
    }

}