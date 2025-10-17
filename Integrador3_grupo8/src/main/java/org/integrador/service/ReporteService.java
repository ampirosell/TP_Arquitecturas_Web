package org.integrador.service;

import org.integrador.DTO.ReporteDTO;
import org.integrador.repository.EstudianteDeCarreraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReporteService {

    @Autowired
    private EstudianteDeCarreraRepository estudianteDeCarreraRepository;

    // h) Generar reporte de carreras con inscriptos y egresados por año
    public List<ReporteDTO> generarReporteCarrerasPorAno() {
        return estudianteDeCarreraRepository.findReporteCarrerasPorAno();
    }
}