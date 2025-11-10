package com.example.microserviciomonopatin.controller;


import com.example.microserviciomonopatin.dto.MonopatinKmDTO;
import com.example.microserviciomonopatin.entity.Monopatin;

import com.example.microserviciomonopatin.service.MonopatinService;
//import com.example.microserviciouser.service.UserService;
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

    //UserService userService;


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
    @GetMapping("/monopatines/cercanos")
    public ResponseEntity<?> getMonopatinesCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam double distanciaCercana
    ) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(monopatinService.getMonopatinesCercanos(latitud, longitud, distanciaCercana));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
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
