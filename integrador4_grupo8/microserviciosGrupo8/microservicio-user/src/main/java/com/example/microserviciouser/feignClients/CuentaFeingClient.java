package com.example.microserviciouser.feignClients;

import  org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "microservicio-cuenta", url = "http://localhost:8005/cuenta")

public interface CuentaFeingClient {

}
