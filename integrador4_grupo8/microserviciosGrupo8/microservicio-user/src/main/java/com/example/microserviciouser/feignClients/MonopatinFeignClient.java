package com.example.microserviciouser.feignClients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservicio-monopatin", url="http://localhost:8003/monopatines")
public interface MonopatinFeignClient {

/* c)*\

 */
        //@GetMapping("/{id}")
       // Monopatin getMonopatinById(@PathVariable("id") Long id);



}
