package com.example.microservicioparada.controller;


import com.example.microservicioparada.entity.Parada;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/paradas")
public class ParadaController {

    @Autowired
    com.example.microservicioparada.service.ParadaService paradaService;

    @GetMapping()
    public ResponseEntity<List<Parada>> getAllParadas() throws Exception {
        try{
            List<Parada> paradas = paradaService.getAll();
            return new ResponseEntity<>(paradas, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parada> getParadaById(@PathVariable Long id) throws Exception {
        Optional<Parada> parada = Optional.ofNullable(paradaService.findById(id));
        return parada.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Parada> save(@RequestBody Parada parada) {
        Parada userNew = paradaService.save(parada);
        return ResponseEntity.ok(userNew);
    }
    @GetMapping("/monopatinesCercanos/{x}/{y}")
    public ResponseEntity<?> getMonopatinesCercanos(@PathVariable double x, @PathVariable double y, @RequestParam double distanciaCercana){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(paradaService.getMonopatinesCercanos(x,y, distanciaCercana));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

}
