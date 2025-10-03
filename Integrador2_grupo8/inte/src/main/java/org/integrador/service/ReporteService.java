package org.integrador.service;

import org.integrador.repository.EstudianteDeCarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReporteService {

    @Autowired
    private EstudianteDeCarreraRepository estudianteDeCarreraRepository;

    /**
     * 3) Generar reporte de carreras con información de inscriptos y egresados por año.
     * Las carreras se ordenan alfabéticamente y los años de manera cronológica.
     */
    public Map<String, Map<Integer, Map<String, Long>>> generarReporteCarrerasPorAño() {
        List<Object[]> resultados = estudianteDeCarreraRepository.findReporteGeneralCarrerasPorAño();
        
        Map<String, Map<Integer, Map<String, Long>>> reporte = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            String nombreCarrera = (String) resultado[0];
            Integer año = (Integer) resultado[1];
            Long inscriptos = (Long) resultado[2];
            Long egresados = (Long) resultado[3];
            
            // Inicializar estructura si no existe
            reporte.putIfAbsent(nombreCarrera, new HashMap<>());
            reporte.get(nombreCarrera).putIfAbsent(año, new HashMap<>());
            
            // Agregar datos
            reporte.get(nombreCarrera).get(año).put("inscriptos", inscriptos);
            reporte.get(nombreCarrera).get(año).put("egresados", egresados);
        }
        
        return reporte;
    }

    /**
     * Generar reporte para una carrera específica
     */
    public Map<Integer, Map<String, Long>> generarReporteCarreraPorAño(String nombreCarrera) {
        List<Object[]> resultados = estudianteDeCarreraRepository.findReporteCarreraPorAño(nombreCarrera);
        
        Map<Integer, Map<String, Long>> reporte = new HashMap<>();
        
        for (Object[] resultado : resultados) {
            Integer año = (Integer) resultado[0];
            Long inscriptos = (Long) resultado[1];
            Long egresados = (Long) resultado[2];
            
            Map<String, Long> datosAño = new HashMap<>();
            datosAño.put("inscriptos", inscriptos);
            datosAño.put("egresados", egresados);
            
            reporte.put(año, datosAño);
        }
        
        return reporte;
    }

    /**
     * Formatear reporte para presentación
     */
    public String formatearReporteCarrerasPorAño() {
        Map<String, Map<Integer, Map<String, Long>>> reporte = generarReporteCarrerasPorAño();
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== REPORTE DE CARRERAS POR AÑO ===\n\n");
        
        // Ordenar carreras alfabéticamente
        reporte.entrySet().stream()
               .sorted(Map.Entry.comparingByKey())
               .forEach(entry -> {
                   String carrera = entry.getKey();
                   Map<Integer, Map<String, Long>> datosCarrera = entry.getValue();
                   
                   sb.append("CARRERA: ").append(carrera).append("\n");
                   sb.append("-".repeat(carrera.length() + 10)).append("\n");
                   
                   // Ordenar años cronológicamente
                   datosCarrera.entrySet().stream()
                              .sorted(Map.Entry.comparingByKey())
                              .forEach(añoEntry -> {
                                  Integer año = añoEntry.getKey();
                                  Map<String, Long> datos = añoEntry.getValue();
                                  
                                  sb.append(String.format("  %d: Inscriptos: %d, Egresados: %d\n", 
                                                         año, 
                                                         datos.get("inscriptos"), 
                                                         datos.get("egresados")));
                              });
                   
                   sb.append("\n");
               });
        
        return sb.toString();
    }

    /**
     * Formatear reporte para una carrera específica
     */
    public String formatearReporteCarreraPorAño(String nombreCarrera) {
        Map<Integer, Map<String, Long>> reporte = generarReporteCarreraPorAño(nombreCarrera);
        StringBuilder sb = new StringBuilder();
        
        sb.append("=== REPORTE DE CARRERA: ").append(nombreCarrera).append(" ===\n\n");
        
        if (reporte.isEmpty()) {
            sb.append("No se encontraron datos para esta carrera.\n");
            return sb.toString();
        }
        
        // Ordenar años cronológicamente
        reporte.entrySet().stream()
               .sorted(Map.Entry.comparingByKey())
               .forEach(entry -> {
                   Integer año = entry.getKey();
                   Map<String, Long> datos = entry.getValue();
                   
                   sb.append(String.format("%d: Inscriptos: %d, Egresados: %d\n", 
                                          año, 
                                          datos.get("inscriptos"), 
                                          datos.get("egresados")));
               });
        
        return sb.toString();
    }
}
