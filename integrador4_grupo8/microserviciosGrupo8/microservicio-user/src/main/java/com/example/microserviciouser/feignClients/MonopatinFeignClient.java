package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@FeignClient(name="microservicio-monopatin", url="http://localhost:8003/monopatines")
public interface MonopatinFeignClient {

/* c)*\

 */
        @GetMapping("/{id}")
       Long getMonopatinById(@PathVariable("id") Long id);

/*

*\g. Como usuario quiero buscar un listado de los monopatines cercanos a mi zona, para poder
encontrar un monopatín cerca de mi ubicación
 */    @GetMapping("/cercanos")
        List<Long> obtenerMonopatinesCercanos(
        @RequestParam("latitud") double latitud,
        @RequestParam("longitud") double longitud,
        @RequestParam("distanciaKm") double distanciaKm);


}
