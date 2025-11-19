package com.example.microserviciofacturacion.controller;

import com.example.microserviciofacturacion.entity.Tarifa;
import com.example.microserviciofacturacion.security.RoleValidator;
import com.example.microserviciofacturacion.security.UserRole;
import com.example.microserviciofacturacion.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tarifas")
public class TarifaController {

    @Autowired
    private FacturacionService service;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping("/vigente")
    public Tarifa getTarifaVigente() {
        roleValidator.require(UserRole.ADMIN);
        return service.getTarifaVigente();
    }
    //ejercicio F
    @PostMapping
    public Tarifa nuevaTarifa(@RequestBody Tarifa tarifa) {
        roleValidator.require(UserRole.ADMIN);
        return service.crearTarifa(tarifa);
    }
}
