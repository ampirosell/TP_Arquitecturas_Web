package com.example.microserviciouser.service;

import com.example.microserviciomonopatin.dto.MonopatinDTO;
import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.feignClients.ParadaFeignClient;
import com.example.microserviciouser.feignClients.MonopatinFeignClient;
import com.example.microserviciouser.feignClients.ViajeFeingClient;
import com.example.microserviciouser.feignClients.CuentaFeingClient;
import com.example.microserviciouser.models.Monopatin;
import com.example.microserviciouser.models.Parada;
import com.example.microserviciouser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Autowired
    ViajeFeingClient viajeFeingClient;

    @Autowired
    CuentaFeingClient cuentaFeingClient;

    public UserService(UserRepository userRepository, MonopatinFeignClient monopatinFeignClient, ViajeFeingClient viajeFeingClient
                         , CuentaFeingClient cuentaFeingClient, ParadaFeignClient paradaFeignClient) {
        this.userRepository = userRepository;
        this.monopatinFeignClient = monopatinFeignClient;
        this.viajeFeingClient = viajeFeingClient;
        this.cuentaFeingClient = cuentaFeingClient;
        this.paradaFeignClient = paradaFeignClient;


    }



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
    /*c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año*/


 public List<Long> obtenerMonopatinesConMasViajes(Long idUser, int anio, Long minViajes) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(String.valueOf(user.getRol()))) {
            throw new RuntimeException("No tiene permisos para realizar esta consulta");
        }

        List<Long> idsMonopatines = this.viajeFeingClient.obtenerMonopatinesConMasViajes(anio, minViajes);

     return idsMonopatines;
    }



    public List<Long> obtenerUsuariosConMasViajesPorTipo(Long idAdmin, LocalDateTime desde, LocalDateTime hasta, String tipoUsuario) {

        User admin = userRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(String.valueOf(admin.getRol()))) {
            throw new RuntimeException("No tiene permisos para realizar esta consulta");
        }


        List<Long> idsUsuariosMasViajes = this.viajeFeingClient.obtenerUsuariosConMasViajes(desde, hasta);


        return idsUsuariosMasViajes;
    }







}
