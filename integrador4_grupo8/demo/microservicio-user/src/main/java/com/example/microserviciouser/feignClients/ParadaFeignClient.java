package com.example.microserviciouser.feignClients;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="microservicio-parada", url="http://localhost:8002/paradas")
public interface ParadaFeignClient {

//falta aclarar metodos, ejemplo:

/*@PostMapping
    Bike save(@RequestBody Bike bike);

    @GetMapping("/byUser/{userId}")
    List<Bike> getBikes(@PathVariable("userId") Long userId);*/
}
