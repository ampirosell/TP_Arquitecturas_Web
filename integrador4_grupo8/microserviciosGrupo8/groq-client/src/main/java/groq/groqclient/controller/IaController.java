package groq.groqclient.controller;

import groq.groqclient.service.IaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class IaController {

    private final IaService iaService;

    @PostMapping
    public String consultar(@RequestBody String mensaje) {
        return iaService.chat(mensaje);
    }
}
