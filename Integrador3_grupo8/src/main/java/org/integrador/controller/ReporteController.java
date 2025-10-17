package org.integrador.controller;

import org.integrador.DTO.ReporteDTO;
import org.integrador.service.ReporteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    // h) Generar reporte de carreras con inscriptos y egresados por año
    @GetMapping("/carreras-por-ano")
    public ResponseEntity<List<ReporteDTO>> generarReporteCarrerasPorAno() {
        List<ReporteDTO> reporte = reporteService.generarReporteCarrerasPorAno();
        return ResponseEntity.ok(reporte);
    }
}