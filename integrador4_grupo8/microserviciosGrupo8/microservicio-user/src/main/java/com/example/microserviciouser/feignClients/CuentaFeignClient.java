package com.example.microserviciouser.feignClients;

import com.example.microserviciouser.dto.ActualizarEstadoCuentaRequest;
<<<<<<< HEAD
import  org.springframework.cloud.openfeign.FeignClient;

=======
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
>>>>>>> origin/integrador4-v2
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

<<<<<<< HEAD

@FeignClient(name = "microservicio-cuenta", url = "http://localhost:8005/cuenta")

public interface CuentaFeignClient {
//ejercicio B
    @PatchMapping("/{id}/estado")
    Object actualizarEstado(
            @PathVariable Long id,
            @RequestBody ActualizarEstadoCuentaRequest request);

}
=======
@FeignClient(name = "microservicio-cuenta", url = "http://localhost:8005/cuenta")
public interface CuentaFeignClient {

    @PatchMapping("/{id}/estado")
    Object actualizarEstado(@PathVariable("id") Long id, @RequestBody ActualizarEstadoCuentaRequest request);

    @GetMapping("/cuentas/premium/{idUsuario}")
    Boolean esUsuarioPremium(@PathVariable Long idUsuario);
}

>>>>>>> origin/integrador4-v2
