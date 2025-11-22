package groq.groqclient.service;

import groq.groqclient.client.GroqClient;
import groq.groqclient.feign.UserFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IaService {

    private final GroqClient groqClient;
    private final UserFeign userFeign;
    private final ToolService toolService;

    public String pedirRespuestaIA(Long userId, String pregunta) {

        // Validación via microservicio-user
        Boolean premium = userFeign.esPremium(userId);
        if (!premium) {
            throw new RuntimeException("Solo usuarios PREMIUM pueden usar el chat IA");
        }

        // Llamada al motor LLM con tools
        return groqClient.consultarLLM(pregunta, toolService.obtenerTools());
    }
}
