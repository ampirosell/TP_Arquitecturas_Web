package org.integrador.controller;

import org.integrador.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // 3) Reporte general de carreras por año
    @GetMapping("/carreras-por-ano")
    public ResponseEntity<Map<String, Map<Integer, Map<String, Long>>>> generarReporteCarrerasPorAño() {
        Map<String, Map<Integer, Map<String, Long>>> reporte = reporteService.generarReporteCarrerasPorAño();
        return ResponseEntity.ok(reporte);
    }



    // Reporte de una carrera específica por año
    @GetMapping("/carrera/{nombreCarrera}/por-ano")
    public ResponseEntity<Map<Integer, Map<String, Long>>> generarReporteCarreraPorAño(
            @PathVariable String nombreCarrera) {
        Map<Integer, Map<String, Long>> reporte = reporteService.generarReporteCarreraPorAño(nombreCarrera);
        return ResponseEntity.ok(reporte);
    }


}
