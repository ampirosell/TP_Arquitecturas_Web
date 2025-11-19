package com.example.microserviciomonopatin.controller;


import com.example.microserviciomonopatin.dto.*;
import com.example.microserviciomonopatin.entity.Monopatin;

import com.example.microserviciomonopatin.security.RoleValidator;
import com.example.microserviciomonopatin.security.UserRole;
import com.example.microserviciomonopatin.service.MonopatinService;
//import com.example.microserviciouser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

    @Autowired
    MonopatinService monopatinService;

    @Autowired
    private RoleValidator roleValidator;

    //UserService userService;


    @GetMapping()
    public ResponseEntity<List<Monopatin>> getAllMonopatines() throws Exception {
        roleValidator.require(UserRole.ADMIN);
        try{
            List<Monopatin> monopatines = monopatinService.getAll();
            return new ResponseEntity<>(monopatines, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> getMonopatinById(@PathVariable Long id) throws Exception {
        roleValidator.require(UserRole.ADMIN);
        Optional<Monopatin> monopatin = Optional.ofNullable(monopatinService.findById(id));
        return monopatin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Monopatin> save(@RequestBody Monopatin monopatin) {
        roleValidator.require(UserRole.ADMIN);
        Monopatin userNew = monopatinService.save(monopatin);
        return ResponseEntity.ok(userNew);
    }
    @GetMapping("/cercanos")
    public ResponseEntity<?> getMonopatinesCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam double distanciaCercana
    ) {
        roleValidator.require(UserRole.USER, UserRole.ADMIN);
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(monopatinService.getMonopatinesCercanos(latitud, longitud, distanciaCercana));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                              @RequestBody @Valid ActualizarEstadoMonopatinRequest request) {
        roleValidator.require(UserRole.ADMIN, UserRole.MAINTENANCE);
        if (request.getEstado() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Debe indicar un estado válido\"}");
        }
        Monopatin actualizado = monopatinService.actualizarEstado(id, request.getEstado(), request.getViajeId());
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @PatchMapping("/{id}/ubicacion")
    public ResponseEntity<?> actualizarUbicacion(@PathVariable Long id,
                                                 @RequestBody @Valid ActualizarUbicacionMonopatinRequest request) {
        roleValidator.require(UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.actualizarUbicacion(id, request.getLatitud(), request.getLongitud(), request.getParadaId());
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/mantenimiento")
    public ResponseEntity<?> registrarMantenimiento(@PathVariable Long id) {
        roleValidator.require(UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.registrarMantenimiento(id);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}/mantenimiento")
    public ResponseEntity<?> finalizarMantenimiento(@PathVariable Long id) {
        roleValidator.require(UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.finalizarMantenimiento(id);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/resumen/estado")
    public ResponseEntity<ResumenEstadoMonopatinesDTO> obtenerResumenEstados() {
        roleValidator.require(UserRole.ADMIN);
        return ResponseEntity.ok(monopatinService.obtenerResumenEstados());
    }

    @PatchMapping("/{id}/kilometros")
    public ResponseEntity<?> actualizarKilometros(@PathVariable Long id,
                                                  @RequestBody @Valid ActualizarKilometrosMonopatinRequest request) {
        roleValidator.require(UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.actualizarKilometros(id, request.getKilometrosRecorridos());
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/cercanos")
    public ResponseEntity<List<Long>> obtenerMonopatinesCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam(defaultValue = "3.0") double distanciaKm) {

        List<Long> cercanos = this.monopatinService.getMonopatinesCercanos(latitud, longitud, distanciaKm);
        return ResponseEntity.ok(cercanos);
    }

    // Reporte de km recorridos (solo para ADMIN)  ejercicio 4 a) ESTO NO VA SI LO PONEMOS EN EL VIAJE
/*
    @GetMapping("/reporte/km/{userId}")
    public ResponseEntity<?> getReporteKm(@PathVariable Long userId) {
        List<MonopatinKmDTO> reporte = monopatinService.generarReporteKm();
        return ResponseEntity.ok(reporte);
    }
*/
    //@GetMapping("/cantViajesPorAnio/{idAdmin}/{cantViajes}/{anio}")

}
