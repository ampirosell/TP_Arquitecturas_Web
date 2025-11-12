package com.example.microservicioparada.controller;


import com.example.microservicioparada.entity.Parada;
import com.example.microservicioparada.security.RoleValidator;
import com.example.microservicioparada.security.UserRole;
import com.example.microservicioparada.service.ParadaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/paradas")
public class ParadaController {

    @Autowired
    ParadaService paradaService;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping()
    public ResponseEntity<List<Parada>> getAllParadas(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader) throws Exception {
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        try {
            List<Parada> paradas = paradaService.getAll();
            return new ResponseEntity<>(paradas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parada> getParadaById(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id) throws Exception {
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        Parada parada = paradaService.findById(id);
        if (parada != null) {
            return ResponseEntity.ok(parada);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Parada> save(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestBody Parada parada) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        Parada userNew = paradaService.save(parada);
        return ResponseEntity.ok(userNew);
    }

    @GetMapping("/monopatinesCercanos")
    public ResponseEntity<?> getMonopatinesCercanos(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestParam double latitud, @RequestParam double longitud, @RequestParam double distanciaCercana) {
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        try {
            return ResponseEntity.ok(
                    paradaService.getMonopatinesCercanos(latitud, longitud, distanciaCercana)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

}
