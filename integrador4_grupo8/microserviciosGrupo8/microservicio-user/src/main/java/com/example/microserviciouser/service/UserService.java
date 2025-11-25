package com.example.microserviciouser.service;


import com.example.microserviciouser.dto.ActualizarEstadoCuentaRequest;
<<<<<<< HEAD
import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.feignClients.*;
=======
import com.example.microserviciouser.dto.AuthResponse;
import com.example.microserviciouser.dto.LoginRequest;
import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import com.example.microserviciouser.feignClients.*;
import com.example.microserviciouser.security.JwtUtil;
import com.example.microserviciouser.security.UserRole;
>>>>>>> origin/integrador4-v2

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

<<<<<<< HEAD
=======
    @Autowired
    JwtUtil jwtUtil;

>>>>>>> origin/integrador4-v2
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

    public AuthResponse login(LoginRequest request) {
        User user;
        
        if (request.getUserId() != null) {
            user = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getUserId()));
        } else if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findAll().stream()
                    .filter(u -> request.getEmail().equalsIgnoreCase(u.getMail()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + request.getEmail()));
        } else {
            throw new RuntimeException("Debe proporcionar userId o email");
        }

        UserRole userRole = convertRolToUserRole(user.getRol());
        String token = jwtUtil.generateToken(user.getMail() != null ? user.getMail() : String.valueOf(user.getId()), 
                                             user.getId(), 
                                             userRole);

        return new AuthResponse(
                token,
                user.getId(),
                user.getMail(),
                user.getRol().name(),
                "Login exitoso"
        );
    }

    public AuthResponse register(User user) {
        // Validar que el email no esté duplicado
        if (user.getMail() != null && !user.getMail().isEmpty()) {
            boolean emailExists = userRepository.findAll().stream()
                    .anyMatch(u -> user.getMail().equalsIgnoreCase(u.getMail()));
            if (emailExists) {
                throw new RuntimeException("Ya existe un usuario con el email: " + user.getMail());
            }
        }

        // Si no se especifica rol, asignar USER por defecto
        if (user.getRol() == null) {
            user.setRol(Rol.USER);
        }

        // Guardar el usuario
        User savedUser = userRepository.save(user);

        // Generar token JWT
        UserRole userRole = convertRolToUserRole(savedUser.getRol());
        String token = jwtUtil.generateToken(
                savedUser.getMail() != null ? savedUser.getMail() : String.valueOf(savedUser.getId()),
                savedUser.getId(),
                userRole
        );

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getMail(),
                savedUser.getRol().name(),
                "Registro exitoso"
        );
    }

    private UserRole convertRolToUserRole(Rol rol) {
        if (rol == null) {
            return UserRole.USER;
        }
        try {
            return UserRole.valueOf(rol.name());
        } catch (IllegalArgumentException e) {
            return UserRole.USER;
        }
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

<<<<<<< HEAD
        // Se pasa el header con el rol del usuario (ADMIN)
        return facturacionFeignClient.obtenerTotalFacturadoEnMeses(
                anio,
                mesInicio,
                mesFin,
                admin.getRol().name()
        );
=======
        return facturacionFeignClient.obtenerTotalFacturadoEnMeses(anio, mesInicio, mesFin);
>>>>>>> origin/integrador4-v2
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

<<<<<<< HEAD
        return facturacionFeignClient.crearTarifa("ADMIN", tarifa);
=======
        return facturacionFeignClient.crearTarifa(tarifa);
>>>>>>> origin/integrador4-v2
    }

    // G)
    public List<Long> buscarMonopatinesCercanos(double latitud, double longitud, double distanciaKm) {
        return this.monopatinFeignClient.obtenerMonopatinesCercanos(latitud, longitud, distanciaKm);
    }

    //ejercicio 10 llm

    public boolean usuarioEsPremium(Long idUsuario) {
        return cuentaFeignClient.esUsuarioPremium(idUsuario);
    }


}
