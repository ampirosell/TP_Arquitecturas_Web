package groq.groqclient.client;

import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

@Component
public class GroqClient {

    private final OpenAiChatModel model;

    public GroqClient(OpenAiChatModel model) {
        this.model = model;
    }

    public String chat(String prompt) {
        return model.call(new Prompt(prompt)).getResult().getOutputText();
    }
}
