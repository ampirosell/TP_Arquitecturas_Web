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
}
