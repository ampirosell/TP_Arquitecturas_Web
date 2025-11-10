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
/*    //ejercicio B
    @PutMapping("/deshabilitar/{idAdmin}/{idUsuario}")
    public ResponseEntity<?> deshabilitarCuenta(@PathVariable Long idAdmin, @PathVariable Long idUsuario) {
        if (!userService.esAdmin(idAdmin)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No tiene permisos para deshabilitar cuentas");
        }

        Cuenta cuentaActualizada = cuentaService.actualizarEstadoCuenta(idUsuario, false);
        if (cuentaActualizada == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cuenta no encontrada para el usuario con ID: " + idUsuario);
        }

        return ResponseEntity.ok(cuentaActualizada);
    }
*/
}
