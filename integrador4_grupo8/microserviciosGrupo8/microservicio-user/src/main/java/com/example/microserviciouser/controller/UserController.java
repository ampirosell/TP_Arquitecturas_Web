package com.example.microserviciouser.controller;

import com.example.microserviciouser.entity.User;
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

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            return  ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return  ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping()
    public ResponseEntity<User> save(@RequestBody User user) {
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
