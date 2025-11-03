package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="microservicio-monopatin", url="http://localhost:8003/monopatin")
public interface MonopatinFeignClient {



}
