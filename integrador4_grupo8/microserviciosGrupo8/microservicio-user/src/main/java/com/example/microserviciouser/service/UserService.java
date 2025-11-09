package com.example.microserviciouser.service;

import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.feignClients.ParadaFeignClient;
import com.example.microserviciouser.feignClients.MonopatinFeignClient;
import com.example.microserviciouser.models.Monopatin;
import com.example.microserviciouser.models.Parada;
import com.example.microserviciouser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    MonopatinFeignClient monopatinFeignClient;

    @Autowired
    ParadaFeignClient paradaFeignClient;

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public User save(User user){
        User userNew;
        userNew = userRepository.save(user);
        return userNew;
    }
    public void delete(User user){

        userRepository.delete(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User update(User user){
        return userRepository.save(user);
    }

    //ejercicio a
    public boolean esAdmin(Long id) {
        Rol rol = userRepository.findRolById(id);
        return rol == Rol.ADMIN;
    }
}
