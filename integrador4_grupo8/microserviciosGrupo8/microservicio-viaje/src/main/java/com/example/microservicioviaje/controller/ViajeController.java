package com.example.microservicioviaje.controller;

import com.example.microservicioviaje.dto.FinalizarViajeRequest;
import com.example.microservicioviaje.dto.IniciarViajeRequest;
import com.example.microservicioviaje.dto.MonopatinViajesDTO;
import com.example.microservicioviaje.dto.ReporteUsoMonopatinDTO;
import com.example.microservicioviaje.entity.Viaje;
import com.example.microservicioviaje.security.RoleValidator;
import com.example.microservicioviaje.security.UserRole;
import com.example.microservicioviaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/viajes")
public class ViajeController {

    @Autowired
    ViajeService viajeService;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping()
    public ResponseEntity<List<Viaje>> getAllViajes() throws Exception {
        roleValidator.require(UserRole.ADMIN);
        try {
            List<Viaje> viajes = viajeService.getAll();
            return new ResponseEntity<>(viajes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Viaje> getViajeById(@PathVariable Long id) throws Exception {
        roleValidator.require(UserRole.ADMIN);
        Viaje viaje = viajeService.findById(id);
        if (viaje != null) {
            return ResponseEntity.ok(viaje);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Viaje> save(@RequestBody Viaje viaje) {
        roleValidator.require(UserRole.ADMIN);
        Viaje userNew = viajeService.save(viaje);
        return ResponseEntity.ok(userNew);
    }

    @PostMapping("/start")
    public ResponseEntity<?> iniciarViaje(@RequestBody @Valid IniciarViajeRequest request) {
        roleValidator.require(UserRole.USER, UserRole.ADMIN);
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
        roleValidator.require(UserRole.USER, UserRole.ADMIN);
        try {
            Viaje viaje = viajeService.finalizarViaje(id, request);
            return ResponseEntity.ok(viaje);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(String.format("{\"error\":\"%s\"}", ex.getMessage()));
        }
    }
    //ejercicio A
    @GetMapping("/reportes/kilometros")
    public ResponseEntity<List<ReporteUsoMonopatinDTO>> obtenerReporteKilometros(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        roleValidator.require(UserRole.ADMIN);
        List<ReporteUsoMonopatinDTO> reporte = viajeService.generarReporteUso(desde, hasta);
        if (reporte.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }
/*
e. Como administrador quiero ver los usuarios que más utilizan los monopatines, filtrado por
período y por tipo de usuario
 */

    @GetMapping("/reportes/monopatines-frecuentes")
    public ResponseEntity<List<Long>> obtenerMonopatinesConMasViajes(
            @RequestParam int anio,
            @RequestParam long minViajes
    ) {
        roleValidator.require(UserRole.ADMIN);
        List<Long> resultado = viajeService.obtenerMonopatinesConMasViajes(anio, minViajes);
        if (resultado.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(resultado);
    }


         @GetMapping("/monopatines-mas-viajes/{anio}/{minViajes}")
    public List<Long> obtenerMonopatinesConMasViajes(
            @PathVariable int anio,
            @PathVariable Long minViajes) {
        return viajeService.obtenerMonopatinesConMasViajes(anio, minViajes);}

// e)
    @GetMapping("/usuarios-mas-viajes")
    public ResponseEntity<List<Long>> getUsuariosConMasViajes(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta) {

        List<Long> usuarios = viajeService.obtenerUsuariosConMasViajesPorPeriodo(desde, hasta);
        return ResponseEntity.ok(usuarios);
    }

    //  EJERCICIO H
    @GetMapping("/usado-en-periodo")
    public ResponseEntity<?> obtenerUsoPorUsuario(
            @RequestParam Long idUsuario,
            @RequestParam(required = false) Long idCuenta,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        roleValidator.require(UserRole.USER, UserRole.ADMIN);

        var reporte = viajeService.obtenerUsoPorUsuario(idUsuario, idCuenta, desde, hasta);
        if (reporte == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reporte);
    }
}
