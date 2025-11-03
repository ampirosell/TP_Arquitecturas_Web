package com.example.microserviciouser.feignClients;


import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="microservicio-parada", url="http://localhost:8002/paradas")
public interface ParadaFeignClient {


}
