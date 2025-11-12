package com.example.microservicioviaje.controller;

import com.example.microservicioviaje.dto.FinalizarViajeRequest;
import com.example.microservicioviaje.dto.IniciarViajeRequest;
import com.example.microservicioviaje.dto.MonopatinViajesDTO;
import com.example.microservicioviaje.dto.ReporteUsoMonopatinDTO;
import com.example.microservicioviaje.entity.Viaje;
import com.example.microservicioviaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    ViajeService viajeService;

    @GetMapping()
    public ResponseEntity<List<Viaje>> getAllViajes() throws Exception {
        try {
            List<Viaje> viajes = viajeService.getAll();
            return new ResponseEntity<>(viajes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> getViajeById(@PathVariable Long id) throws Exception {
        Viaje viaje = viajeService.findById(id);
        if (viaje != null) {
            return ResponseEntity.ok(viaje);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Viaje> save(@RequestBody Viaje viaje) {
        Viaje userNew = viajeService.save(viaje);
        return ResponseEntity.ok(userNew);
    }

    @PostMapping("/start")
    public ResponseEntity<?> iniciarViaje(@RequestBody @Valid IniciarViajeRequest request) {
        try {
            Viaje viaje = viajeService.iniciarViaje(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(viaje);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("{\"error\":\"%s\"}", ex.getMessage()));
        }
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<?> finalizarViaje(@PathVariable Long id,
                                            @RequestBody @Valid FinalizarViajeRequest request) {
        try {
            Viaje viaje = viajeService.finalizarViaje(id, request);
            return ResponseEntity.ok(viaje);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("{\"error\":\"%s\"}", ex.getMessage()));
        }
    }

    @GetMapping("/reportes/kilometros")
    public ResponseEntity<List<ReporteUsoMonopatinDTO>> obtenerReporteKilometros(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            @RequestParam(defaultValue = "true") boolean incluirPausas
    ) {
        List<ReporteUsoMonopatinDTO> reporte = viajeService.generarReporteUso(desde, hasta, incluirPausas);
        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/reportes/monopatines-frecuentes")
    public ResponseEntity<List<MonopatinViajesDTO>> obtenerMonopatinesConMasViajes(
            @RequestParam int anio,
            @RequestParam long minViajes
    ) {
        List<MonopatinViajesDTO> resultado = viajeService.obtenerMonopatinesConMasViajes(anio, minViajes);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }


    /* c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.

         @GetMapping("/monopatines-mas-viajes/{anio}/{minViajes}")
    public List<Long> obtenerMonopatinesConMasViajes(
            @PathVariable int anio,
            @PathVariable int minViajes) {
        return viajeService.obtenerMonopatinesConMasViajes(anio, minViajes);
    }
    *\
     */




}
