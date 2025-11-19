package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "microservicio-facturacion", url = "http://localhost:8010")
public interface FacturacionFeignClient {

    @GetMapping("/facturacion/total-mensual")
    double obtenerTotalFacturadoEnMeses(
            @RequestParam("anio") int anio,
            @RequestParam("mesInicio") int mesInicio,
            @RequestParam("mesFin") int mesFin);

    @PostMapping("/tarifas")
    Object crearTarifa(@RequestBody Object tarifa);
}

