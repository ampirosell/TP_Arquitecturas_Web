package com.example.microserviciofacturacion.controller;

import com.example.microserviciofacturacion.entity.Factura;
import com.example.microserviciofacturacion.security.RoleValidator;
import com.example.microserviciofacturacion.security.UserRole;
import com.example.microserviciofacturacion.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/facturacion")
public class FacturacionController {

    @Autowired
    private FacturacionService service;

    @Autowired
    private RoleValidator roleValidator;

    @PostMapping("/generar")
    public Factura generar(@RequestParam Long idCuenta,
                           @RequestParam Long idViaje,
                           @RequestParam double km,
                           @RequestParam long minutos,
                           @RequestParam boolean pausaExtendida) {
        roleValidator.require(UserRole.ADMIN, UserRole.SYSTEM);
        return service.generarFactura(idCuenta, idViaje, km, minutos, pausaExtendida);
    }

    @GetMapping("/total")
    public double total(@RequestParam String desde,
                        @RequestParam String hasta) {
        roleValidator.require(UserRole.ADMIN);
        return service.totalFacturadoEntre(LocalDate.parse(desde), LocalDate.parse(hasta));
    }
    //ejercicio D
    @GetMapping("/total-mensual")
    public double totalPorMeses(@RequestParam int anio,
                                @RequestParam int mesInicio,
                                @RequestParam int mesFin) {
        roleValidator.require(UserRole.ADMIN);
        return service.totalFacturadoEnMeses(anio, mesInicio, mesFin);
    }
}
