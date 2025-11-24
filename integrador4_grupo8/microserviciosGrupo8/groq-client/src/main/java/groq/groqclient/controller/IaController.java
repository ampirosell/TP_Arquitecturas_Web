
package groq.groqclient.controller;

import lombok.RequiredArgsConstructor;
import groq.groqclient.service.IaService;
import groq.groqclient.dto.RequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ia")
@RequiredArgsConstructor
public class IaController {
    // IaController exponene el endpoint REST que recibe prompts y delega a IaService.
    /**🔑 que va a hacer mi app en conjunto
     *  IaController recibe prompt →
     *  IaService añade esquema + manda a Ollama →
     *  OllamaClient se conecta a la API →
     *  Respuesta: IA devuelve consulta SQL →
     *  IaService la ejecuta →
     *  Respuesta JSON con resultados.
     */

    private final IaService iaService;

    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "message", "Microservicio IA funcionando correctamente",
                "timestamp", String.valueOf(System.currentTimeMillis())
        ));
    }

    @PostMapping(value = "/prompt", produces = "application/json")
    public ResponseEntity<?> procesarPrompt(@RequestBody RequestDTO request) {
        try {
            if (request.getPrompt() == null || request.getPrompt().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El campo 'prompt' es requerido y no puede estar vacío"));
            }
            return iaService.procesarPrompt(request.getPrompt());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el prompt: " + e.getMessage()));
        }
    }
}
