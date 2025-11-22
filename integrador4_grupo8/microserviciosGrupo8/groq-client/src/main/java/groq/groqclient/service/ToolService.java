package groq.groqclient.service;

import groq.groqclient.feign.CuentaFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final CuentaFeign cuentaFeign;

    public String usoCuenta() {
        return cuentaFeign.obtenerUsoCuenta();
    }

}
