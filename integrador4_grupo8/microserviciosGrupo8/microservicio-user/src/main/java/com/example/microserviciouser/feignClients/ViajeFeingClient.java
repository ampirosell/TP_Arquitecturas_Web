package com.example.microserviciouser.feignClients;
import  org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "microservicio-viaje", url = "http://localhost:8003/viajes")

public interface ViajeFeingClient{


 @GetMapping("/viajes/monopatines-mas-viajes/{anio}/{minViajes}")
 List<Long> obtenerMonopatinesConMasViajes(@PathVariable("anio") int anio,
                                           @PathVariable("minViajes") Long minViajes);





}






