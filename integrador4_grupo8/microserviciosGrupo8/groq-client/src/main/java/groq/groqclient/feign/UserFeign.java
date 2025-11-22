package groq.groqclient.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-user", url = "http://localhost:8001")
public interface UserFeign {

    @GetMapping("/usuarios/{id}/premium")
    Boolean esPremium(@PathVariable Long id);
}
