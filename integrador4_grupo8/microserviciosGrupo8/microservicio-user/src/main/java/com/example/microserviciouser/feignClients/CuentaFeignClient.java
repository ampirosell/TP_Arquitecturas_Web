package com.example.microserviciouser.feignClients;

import com.example.microserviciouser.dto.ActualizarEstadoCuentaRequest;
import  org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "microservicio-cuenta", url = "http://localhost:8005/cuenta")

public interface CuentaFeignClient {
//ejercicio B
    @PatchMapping("/{id}/estado")
    Object actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoCuentaRequest request);

}
