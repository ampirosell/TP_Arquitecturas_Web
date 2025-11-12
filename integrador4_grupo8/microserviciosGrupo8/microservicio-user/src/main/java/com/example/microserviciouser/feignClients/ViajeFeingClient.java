package com.example.microserviciouser.feignClients;
import  org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "microservicio-viaje", url = "http://localhost:8003/viajes")

public interface ViajeFeingClient{

/*c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.
 @GetMapping("/viajes/monopatines-mas-viajes/{anio}/{minViajes}")
    List<Long> obtenerMonopatinesConMasViajes(@PathVariable("anio") int anio,
                                              @PathVariable("minViajes") int minViajes);
}*\
 */


}
