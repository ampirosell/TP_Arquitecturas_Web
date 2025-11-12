package com.example.microservicioviaje.controller;


import com.example.microservicioviaje.dto.PausaDTO;
import com.example.microservicioviaje.entity.Pausa;

import com.example.microservicioviaje.service.PausaService;
import com.example.microservicioviaje.security.RoleValidator;
import com.example.microservicioviaje.security.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/pausas")
public class PausaController {

    @Autowired
    PausaService pausaService;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping()
    public ResponseEntity<?> getAll(@RequestHeader(value = "X-User-Role", required = false) String roleHeader){
        roleValidator.require(roleHeader, UserRole.ADMIN);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pausaService.findAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"Error. Por favor intente más tarde.\"}");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPausaById(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id) throws Exception {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        PausaDTO pausa = pausaService.findById(id);
        if (pausa != null) {
            return ResponseEntity.ok(pausa);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping()
    public ResponseEntity<?> save(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestBody Pausa p){
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pausaService.save(p));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo ingresar, revise los campos e intente nuevamente.\"}");
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id,@RequestBody Pausa entity){
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        try{
            PausaDTO updated = pausaService.update(id,entity);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"error\":\"No se encontró la pausa\"}");
            }
            return ResponseEntity.status(HttpStatus.OK).body(updated);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. No se pudo editar, o no se encontró el ID. Revise los campos e intente nuevamente.\"}");
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable Long id){
        roleValidator.require(roleHeader, UserRole.USER, UserRole.ADMIN);
        try{
            return ResponseEntity.status(HttpStatus.OK).body(pausaService.delete(id));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error. no se pudo eliminar intente nuevamente.\"}");
        }
    }

}
