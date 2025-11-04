package com.example.microservicioparada.controller;


import com.example.microservicioparada.entity.Parada;
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

    @GetMapping()
    public ResponseEntity<List<Parada>> getAllParadas() throws Exception {
        try {
            List<Parada> paradas = paradaService.getAll();
            return new ResponseEntity<>(paradas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parada> getParadaById(@PathVariable Long id) throws Exception {
        Parada parada = paradaService.findById(id);
        if (parada != null) {
            return ResponseEntity.ok(parada);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<Parada> save(@RequestBody Parada parada) {
        Parada userNew = paradaService.save(parada);
        return ResponseEntity.ok(userNew);
    }

    @GetMapping("/monopatinesCercanos")
    public ResponseEntity<?> getMonopatinesCercanos(@RequestParam double latitud, @RequestParam double longitud, @RequestParam double distanciaCercana) {
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
