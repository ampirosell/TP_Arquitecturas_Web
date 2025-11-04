package com.example.microserviciomonopatin.controller;


import com.example.microserviciomonopatin.entity.Monopatin;

import com.example.microserviciomonopatin.service.MonopatinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monopatines")
public class MonopatinController {

    @Autowired
    MonopatinService monopatinService;

    @GetMapping()
    public ResponseEntity<List<Monopatin>> getAllMonopatines() throws Exception {
        try{
            List<Monopatin> monopatines = monopatinService.getAll();
            return new ResponseEntity<>(monopatines, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Monopatin> getMonopatinById(@PathVariable String id) throws Exception {
        Optional<Monopatin> monopatin = Optional.ofNullable(monopatinService.findById(id));
        return monopatin.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Monopatin> save(@RequestBody Monopatin monopatin) {
        Monopatin userNew = monopatinService.save(monopatin);
        return ResponseEntity.ok(userNew);
    }
    @GetMapping("/monopatinesCercanos/{x}/{y}")
    public ResponseEntity<?> getMonopatinesCercanos(@PathVariable double x, @PathVariable double y, @RequestParam double distanciaCercana){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(monopatinService.getMonopatinesCercanos(x,y, distanciaCercana));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

}
