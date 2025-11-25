package mock.mockmercadopago.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payments")
public class MercadoPagoController {

    @PostMapping
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest req) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", "approved");
        response.put("payment_id", 99999);
        response.put("received_amount", req.getAmount());

        return ResponseEntity.ok(response);
    }
}
