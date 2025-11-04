package com.example.microserviciocuenta.controller;


import com.example.microserviciocuenta.entity.Cuenta;

import com.example.microserviciocuenta.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuenta")
public class CuentaController {

    @Autowired
    CuentaService cuentaService;


    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable String id) throws Exception {
        Optional<Cuenta> cuenta = Optional.ofNullable(cuentaService.findById(id));
        return cuenta.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Cuenta> save(@RequestBody Cuenta cuenta) {
        Cuenta userNew = cuentaService.save(cuenta);
        return ResponseEntity.ok(userNew);
    }


}
