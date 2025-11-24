package groq.groqclient.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseDTO<T> {
    private boolean ok;
    private String mensaje;
    private T datos;
}