package com.example.microserviciocuenta.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "mercadopago", url = "${mercadopago.url}")
public interface MercadoPagoFeign {

    @PostMapping("/payments")
    Map<String, Object> procesarPago(@RequestBody Map<String, Object> pago);
}

