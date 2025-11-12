package com.example.microserviciouser.controller;

import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.security.RoleValidator;
import com.example.microserviciouser.security.UserRole;
import com.example.microserviciouser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    /* c)
         @GetMapping("/{idUser}/monopatines-mas-viajes/{anio}/{minViajes}")
    public List<MonopatinDTO> obtenerMonopatinesConMasViajes(
            @PathVariable Long idUser,
            @PathVariable int anio,
            @PathVariable int minViajes) {
        return userService.obtenerMonopatinesConMasViajes(idUser, anio, minViajes);
    }

    *\
     */

}
