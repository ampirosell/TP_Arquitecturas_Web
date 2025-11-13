package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "microservicio-facturacion", url = "http://localhost:8010/facturacion")
public interface FacturacionFeignClient {

    // ejercicio D)
    @GetMapping("/total-mensual")
    Double obtenerTotalFacturadoEnMeses(
            @RequestParam("anio") int anio,
            @RequestParam("mesInicio") int mesInicio,
            @RequestParam("mesFin") int mesFin,
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader
    );
    // Ejercicio F - Crear nueva tarifa
    @PostMapping
    Object crearTarifa(
            @RequestHeader(value = "X-User-Role", required = false) String roleHeader,
            @RequestBody Object tarifa
    );

}
