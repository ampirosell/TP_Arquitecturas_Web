package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "microservicio-viaje", url = "http://localhost:8004/viajes")
public interface ViajeFeignClient {
    /*
     * a) Como administrador quiero poder generar un reporte de uso de monopatines por kilómetros
     * para establecer si un monopatín requiere de mantenimiento.
     * Este reporte debe poder configurarse para incluir (o no) los tiempos de pausa.
     */
    @GetMapping("/reportes/kilometros")
    List<Object> obtenerReporteKilometros(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate desde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate hasta
    );

    // Ejercicio E: monopatines con más viajes
    @GetMapping("/monopatines-mas-viajes/{anio}/{minViajes}")
    List<Long> obtenerMonopatinesConMasViajes(
            @PathVariable("anio") int anio,
            @PathVariable("minViajes") Long minViajes);

    // Ejercicio E: usuarios con más viajes por período
    @GetMapping("/usuarios-mas-viajes")
    List<Long> obtenerUsuariosConMasViajes(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta);

    // Ejercicio H: uso de monopatines por usuario (opcional)
    @GetMapping("/usuarios/{idUsuario}/uso")
    Double obtenerUsoUsuario(
            @PathVariable("idUsuario") Long idUsuario,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta);
}
