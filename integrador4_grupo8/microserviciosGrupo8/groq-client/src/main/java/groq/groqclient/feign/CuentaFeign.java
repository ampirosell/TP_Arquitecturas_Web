package groq.groqclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cuenta-service", url = "http://microservicio-cuenta:8005")
public interface CuentaFeign {

    @GetMapping("/cuenta/uso")
    String obtenerUsoCuenta();
}
