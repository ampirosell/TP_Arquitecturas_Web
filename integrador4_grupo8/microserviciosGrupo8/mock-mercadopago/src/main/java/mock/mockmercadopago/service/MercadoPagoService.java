package mock.mockmercadopago.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MercadoPagoService {

    @Value("${mercadopago.url}")
    private String url;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> procesarPago(Map<String, Object> pago) {
            return restTemplate.postForObject(url, pago, Map.class);
    }
}