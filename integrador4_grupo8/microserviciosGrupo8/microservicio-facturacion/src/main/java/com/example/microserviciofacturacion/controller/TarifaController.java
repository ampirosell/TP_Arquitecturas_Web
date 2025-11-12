package com.example.microserviciofacturacion.controller;

import com.example.microserviciofacturacion.entity.Tarifa;
import com.example.microserviciofacturacion.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    @Autowired
    private FacturacionService service;

    @GetMapping("/vigente")
    public Tarifa getTarifaVigente() {
        return service.getTarifaVigente();
    }

    @PostMapping
    public Tarifa nuevaTarifa(@RequestBody Tarifa tarifa) {
        return service.crearTarifa(tarifa);
    }
}
