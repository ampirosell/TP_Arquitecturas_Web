package com.example.microserviciouser.controller;

import com.example.microserviciouser.dto.ActualizarEstadoCuentaRequest;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.security.RoleValidator;
import com.example.microserviciouser.security.UserRole;
import com.example.microserviciouser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @PathVariable("id") Long id) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        User user = userService.getUserById(id);
        if (user == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> save(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestBody User user) {
        roleValidator.require(roleHeader, UserRole.ADMIN, UserRole.USER);
        User userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }
    //ejercicio a)
    @GetMapping("/reportes/kilometros")
    public Object reporteUsoMonopatines(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta
    ) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        return userService.obtenerReporteKilometros(desde, hasta);
    }
    //ejercicio B
    @PatchMapping("/{id}/estado")
    public Object actualizarEstado(@RequestHeader(value = "X-User-Role", required = false) String roleHeader,@RequestParam Long id, @RequestParam ActualizarEstadoCuentaRequest request){
        return userService.actualizarEstado(id, request);
    }


    /* c)

     */
         @GetMapping("/{idUser}/monopatines-mas-viajes/{anio}/{minViajes}")
    public List<Long> obtenerMonopatinesConMasViajes(
            @PathVariable Long idUser,
            @PathVariable int anio,
            @PathVariable Long minViajes) {
        return userService.obtenerMonopatinesConMasViajes(idUser, anio, minViajes);
    }
    // Ejercicio D - Consultar total facturado en un rango de meses de cierto año
    @GetMapping("/{idAdmin}/total-mensual")
    public double obtenerTotalFacturadoEnMeses(
            @PathVariable Long idAdmin,
            @RequestParam int anio,
            @RequestParam int mesInicio,
            @RequestParam int mesFin,
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader
    ) {
        roleValidator.require(roleHeader, UserRole.ADMIN);
        return userService.obtenerTotalFacturadoEnMeses(idAdmin, anio, mesInicio, mesFin);
    }
// e)
    @GetMapping("/usuarios-mas-viajes")
    public ResponseEntity<List<Long>> getUsuariosMasViajesPorTipo(
            @RequestHeader("X-User-Id") Long idAdmin,
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam("tipoUsuario") String tipoUsuario) {

        List<Long> usuarios = userService.obtenerUsuariosConMasViajesPorTipo(idAdmin, desde, hasta, tipoUsuario);
        return ResponseEntity.ok(usuarios);
    }

    // Ejercicio F - Crear nueva tarifa
    @PostMapping("/{idAdmin}/tarifas")
    public Object crearTarifa(
            @PathVariable Long idAdmin,
            @RequestBody Object tarifa,
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader
    ) {
        return userService.crearNuevaTarifa(idAdmin, tarifa);
    }
    // g)
    @GetMapping("/monopatines-cercanos")
    public ResponseEntity<List<Long>> obtenerMonopatinesCercanos(
            @RequestParam double latitud,
            @RequestParam double longitud,
            @RequestParam(defaultValue = "3.0") double distanciaKm) {

        List<Long> ids = userService.buscarMonopatinesCercanos(latitud, longitud, distanciaKm);
        return ResponseEntity.ok(ids);
    }


}
