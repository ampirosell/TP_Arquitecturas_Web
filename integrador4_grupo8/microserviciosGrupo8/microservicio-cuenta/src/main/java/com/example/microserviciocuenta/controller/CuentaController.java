package com.example.microserviciocuenta.controller;


import com.example.microserviciocuenta.dto.ActualizarEstadoCuentaRequest;
import com.example.microserviciocuenta.entity.Cuenta;

import com.example.microserviciocuenta.security.RoleValidator;
import com.example.microserviciocuenta.security.UserRole;
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

    @Autowired
    private RoleValidator roleValidator;


    @GetMapping()
    public ResponseEntity<List<Cuenta>> getAllCuentas() throws Exception {
        roleValidator.require(UserRole.ADMIN);
        try {
            List<Cuenta> cuentas = cuentaService.getAll();
            return new ResponseEntity<>(cuentas, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> getCuentaById(@PathVariable Long id) throws Exception {
        roleValidator.require(UserRole.ADMIN);
        Optional<Cuenta> cuenta = Optional.ofNullable(cuentaService.findById(id));
        return cuenta.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Cuenta> save(@RequestBody Cuenta cuenta) {
        roleValidator.require(UserRole.ADMIN);
        Cuenta userNew = cuentaService.save(cuenta);
        return ResponseEntity.ok(userNew);
    }
    //ejercicio B
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(@PathVariable Long id,
                                              @RequestBody ActualizarEstadoCuentaRequest request) {
        roleValidator.require(UserRole.ADMIN);
        if (request == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"Debe indicar el nuevo estado\"}");
        }
        Cuenta cuentaActualizada = cuentaService.actualizarEstadoCuentaPorId(id, request.isActiva());
        if (cuentaActualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"Cuenta no encontrada\"}");
        }
        return ResponseEntity.ok(cuentaActualizada);
    }
}
