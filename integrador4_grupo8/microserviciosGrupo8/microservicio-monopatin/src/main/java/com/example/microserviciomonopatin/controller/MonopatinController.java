package com.example.microserviciomonopatin.controller;


import com.example.microserviciomonopatin.dto.ActualizarEstadoMonopatinRequest;
import com.example.microserviciomonopatin.dto.ActualizarKilometrosMonopatinRequest;
import com.example.microserviciomonopatin.dto.ActualizarUbicacionMonopatinRequest;
import com.example.microserviciomonopatin.dto.MonopatinKmDTO;
import com.example.microserviciomonopatin.dto.ResumenEstadoMonopatinesDTO;
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
    public ResponseEntity<List<Monopatin>> getAllMonopatines(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader) throws Exception {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        try{
            List<Monopatin> monopatines = monopatinService.getAll();
            return new ResponseEntity<>(monopatines, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> getMonopatinById(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id) throws Exception {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        Optional<Monopatin> monopatin = Optional.ofNullable(monopatinService.findById(id));
        return monopatin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Monopatin> save(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestBody Monopatin monopatin) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        Monopatin userNew = monopatinService.save(monopatin);
        return ResponseEntity.ok(userNew);
    }
    @GetMapping("/cercanos")
    public ResponseEntity<?> getMonopatinesCercanos(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam double distanciaCercana
    ) {
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
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
                                              @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                              @RequestBody @Valid ActualizarEstadoMonopatinRequest request) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.MAINTENANCE);
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
                                                 @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                                 @RequestBody @Valid ActualizarUbicacionMonopatinRequest request) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.actualizarUbicacion(id, request.getLatitud(), request.getLongitud(), request.getParadaId());
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @PostMapping("/{id}/mantenimiento")
    public ResponseEntity<?> registrarMantenimiento(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.registrarMantenimiento(id);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}/mantenimiento")
    public ResponseEntity<?> finalizarMantenimiento(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.finalizarMantenimiento(id);
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    @GetMapping("/resumen/estado")
    public ResponseEntity<ResumenEstadoMonopatinesDTO> obtenerResumenEstados(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        return ResponseEntity.ok(monopatinService.obtenerResumenEstados());
    }

    @PatchMapping("/{id}/kilometros")
    public ResponseEntity<?> actualizarKilometros(@PathVariable Long id,
                                                  @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
                                                  @RequestBody @Valid ActualizarKilometrosMonopatinRequest request) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.MAINTENANCE);
        Monopatin actualizado = monopatinService.actualizarKilometros(id, request.getKilometrosRecorridos());
        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Monopatín no encontrado\"}");
        }
        return ResponseEntity.ok(actualizado);
    }

    // Reporte de km recorridos (solo para ADMIN)  ejercicio 4 a)

   /* @GetMapping("/reporte/km/{userId}")
    public ResponseEntity<?> getReporteKm(@PathVariable Long userId) {
        if (!userService.esAdmin(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para acceder al reporte");
        }

        List<MonopatinKmDTO> reporte = monopatinService.generarReporteKm();
        return ResponseEntity.ok(reporte);
    }
*/
    //@GetMapping("/cantViajesPorAnio/{idAdmin}/{cantViajes}/{anio}")

}
