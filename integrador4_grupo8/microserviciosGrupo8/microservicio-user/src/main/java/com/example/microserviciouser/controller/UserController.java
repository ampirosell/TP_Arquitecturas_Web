package com.example.microserviciouser.controller;

import com.example.microserviciouser.dto.AuthResponse;
import com.example.microserviciouser.dto.LoginRequest;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.security.RoleValidator;
import com.example.microserviciouser.security.UserRole;
import com.example.microserviciouser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private RoleValidator roleValidator;

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        roleValidator.require(UserRole.ADMIN);
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        roleValidator.require(UserRole.ADMIN);
        User user = userService.getUserById(id);
        if (user == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User user) {
        roleValidator.require(UserRole.ADMIN, UserRole.USER);
        User userNew = userService.save(user);
        return ResponseEntity.ok(userNew);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            AuthResponse response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(
                    new AuthResponse(null, null, null, null, "Error: " + e.getMessage())
            );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        try {
            AuthResponse response = userService.register(user);
            return ResponseEntity.status(201).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(
                    new AuthResponse(null, null, null, null, "Error: " + e.getMessage())
            );
        }
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

// e)
    @GetMapping("/usuarios-mas-viajes")
    public ResponseEntity<List<Long>> getUsuariosMasViajesPorTipo(
            @RequestParam("desde") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime desde,
            @RequestParam("hasta") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime hasta,
            @RequestParam("tipoUsuario") String tipoUsuario,
            jakarta.servlet.http.HttpServletRequest request) {
        
        roleValidator.require(UserRole.ADMIN);
        
        // Obtener userId del JWT (guardado en el filter)
        Long idAdmin = (Long) request.getAttribute("userId");
        if (idAdmin == null) {
            return ResponseEntity.status(403).build();
        }

        List<Long> usuarios = userService.obtenerUsuariosConMasViajesPorTipo(idAdmin, desde, hasta, tipoUsuario);
        return ResponseEntity.ok(usuarios);
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


    //ejercicio 10 llm
    @GetMapping("/{id}/premium")
    public Map<String, Boolean> esPremium(@PathVariable Long id) {
        boolean premium = userService.usuarioEsPremium(id);
        return Map.of("premium", premium);
    }

}
