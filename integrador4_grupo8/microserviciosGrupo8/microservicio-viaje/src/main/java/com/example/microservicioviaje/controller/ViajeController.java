package com.example.microservicioviaje.controller;

import com.example.microservicioviaje.entity.Viaje;
import com.example.microservicioviaje.service.ViajeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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


}
