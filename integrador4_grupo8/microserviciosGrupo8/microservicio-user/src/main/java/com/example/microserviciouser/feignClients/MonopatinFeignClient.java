package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@FeignClient(name = "microservicio-monopatin", url = "http://localhost:8003/monopatines")
public interface MonopatinFeignClient {

    /*
     * c) Como administrador quiero poder consultar un monopatín específico por su ID,
     * para conocer su estado actual o detalles.
     */
    @GetMapping("/{id}")
    Object getMonopatinById(@PathVariable("id") Long id);


    /*
     * f) Como administrador quiero obtener los monopatines que hayan tenido más de X pausas,
     * o hayan recorrido más de X kilómetros, para analizar su desempeño.
     */
    @GetMapping("/filtrar")
    List<Object> obtenerMonopatinesPorPausasOKm(
            @RequestParam(required = false) Integer pausasMinimas,
            @RequestParam(required = false) Double kmMinimos
    );


    /*
     * f2) Como administrador quiero saber cuál fue el último viaje iniciado hasta una fecha determinada
     * (findTopByFechaInicioLessThanEqualOrderByFechaInicioDesc),
     * para ver la última actividad registrada.
     */
    @GetMapping("/ultimo-viaje")
    Object obtenerUltimoViajeAntesDe(
            @RequestParam("fecha") LocalDateTime fecha
    );


    /*
     * g) Como usuario quiero buscar un listado de los monopatines cercanos a mi zona,
     * para poder encontrar un monopatín cerca de mi ubicación.
     */
    @GetMapping("/cercanos")
    List<Long> obtenerMonopatinesCercanos(
            @RequestParam("latitud") double latitud,
            @RequestParam("longitud") double longitud,
            @RequestParam("distanciaKm") double distanciaKm
    );
}
