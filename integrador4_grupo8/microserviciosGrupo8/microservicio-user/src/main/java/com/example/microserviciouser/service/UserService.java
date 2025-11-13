package com.example.microserviciouser.service;


import com.example.microserviciouser.dto.ActualizarEstadoCuentaRequest;
import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.feignClients.*;

import com.example.microserviciouser.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


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
    ViajeFeignClient viajeFeignClient;

    @Autowired
    FacturacionFeignClient facturacionFeignClient;

    @Autowired
    CuentaFeignClient cuentaFeignClient;

    public UserService(UserRepository userRepository, MonopatinFeignClient monopatinFeignClient, ViajeFeignClient viajeFeignClient
                         , CuentaFeignClient cuentaFeignClient, ParadaFeignClient paradaFeignClient) {
        this.userRepository = userRepository;
        this.monopatinFeignClient = monopatinFeignClient;
        this.viajeFeignClient = viajeFeignClient;
        this.cuentaFeignClient = cuentaFeignClient;
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
    public Object obtenerReporteKilometros(LocalDate desde, LocalDate hasta) {
        // Llama directamente al microservicio de viajes usando Feign
        return viajeFeignClient.obtenerReporteKilometros( desde, hasta);
    }

    public boolean esAdmin(Long id) {
        Rol rol = userRepository.findRolById(id);
        return rol == Rol.ADMIN;
    }

    //ejercicio B
    public Object actualizarEstado(Long id, ActualizarEstadoCuentaRequest request){
        return cuentaFeignClient.actualizarEstado(id, request);
    }
    /*C. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año*/

    public List<Long> obtenerMonopatinesConMasViajes(Long idUser, int anio, Long minViajes) {
        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(String.valueOf(user.getRol()))) {
            throw new RuntimeException("No tiene permisos para realizar esta consulta");
        }

        List<Long> idsMonopatines = this.viajeFeignClient.obtenerMonopatinesConMasViajes(anio, minViajes);

     return idsMonopatines;
    }



    //  D) Como administrador quiero consultar el total facturado en un rango de meses de cierto año.
    public double obtenerTotalFacturadoEnMeses(Long idAdmin, int anio, int mesInicio, int mesFin) {
        User admin = userRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (admin.getRol() != Rol.ADMIN) {
            throw new RuntimeException("No tiene permisos para realizar esta consulta");
        }

        // Se pasa el header con el rol del usuario (ADMIN)
        return facturacionFeignClient.obtenerTotalFacturadoEnMeses(
                anio,
                mesInicio,
                mesFin,
                admin.getRol().name()
        );
    }
    //ejercicio e Como administrador quiero ver los usuarios que más utilizan los monopatines, filtrado por
    //período y por tipo de usuario.
    public List<Long> obtenerUsuariosConMasViajesPorTipo(Long idAdmin, LocalDateTime desde, LocalDateTime hasta, String tipoUsuario) {

        User admin = userRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(String.valueOf(admin.getRol()))) {
            throw new RuntimeException("No tiene permisos para realizar esta consulta");
        }


        List<Long> idsUsuariosMasViajes = this.viajeFeignClient.obtenerUsuariosConMasViajes(desde, hasta);


        return idsUsuariosMasViajes;
    }
    //F) Como administrador quiero hacer un ajuste de precios, y que a partir de cierta fecha el sistema
    //habilite los nuevos precios
    public Object crearNuevaTarifa(Long idAdmin, Object tarifa) {
        User admin = userRepository.findById(idAdmin)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!"ADMIN".equalsIgnoreCase(String.valueOf(admin.getRol()))) {
            throw new RuntimeException("No tiene permisos para crear tarifas");
        }

        return facturacionFeignClient.crearTarifa("ADMIN", tarifa);
    }

    // G)
    public List<Long> buscarMonopatinesCercanos(double latitud, double longitud, double distanciaKm) {
        return this.monopatinFeignClient.obtenerMonopatinesCercanos(latitud, longitud, distanciaKm);
    }






}
