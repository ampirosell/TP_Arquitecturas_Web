package com.example.microserviciocuenta.service;

import com.example.microserviciocuenta.entity.Cuenta;
import com.example.microserviciocuenta.entity.TipoCuenta;
import com.example.microserviciocuenta.feignClient.MercadoPagoFeign;
import com.example.microserviciocuenta.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;
    @Autowired
    private MercadoPagoFeign mercadoPagoFeign;

    @Transactional
    public Cuenta save(Cuenta cuenta){
        Cuenta cuentaNew;
        cuentaNew = cuentaRepository.save(cuenta);
        return cuentaNew;
    }


    @Transactional
    public List<Cuenta> getAll(){return cuentaRepository.findAll();}

    @Transactional
    public void delete(Cuenta cuenta){
        cuentaRepository.delete(cuenta);
    }

    @Transactional
    public Cuenta update(Cuenta cuenta){
        return cuentaRepository.save(cuenta);
    }

    @Transactional
    public Cuenta findById(Long id) throws Exception {
        return cuentaRepository.findById(id).orElse(null);
    }


    @Transactional
    public Cuenta actualizarEstadoCuentaPorId(Long idCuenta, Boolean cuentaActiva) {
        int updated = cuentaRepository.actualizarEstadoCuentaPorId(idCuenta, cuentaActiva);
        return updated > 0 ? cuentaRepository.findById(idCuenta).orElse(null) : null;
    }

    public boolean esPremium(Long idUsuario) {

        Cuenta cuenta = cuentaRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return cuenta.getCuentaActiva() && cuenta.getTipo() == TipoCuenta.PREMIUM;
    }

    public Map<String, Object> procesarPago(Long idCuenta, Double monto) {

        Cuenta cuenta = cuentaRepository.findById(idCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        // ejemplo: descontar del saldo
        if (cuenta.getMonto() < monto) {
            throw new RuntimeException("Saldo insuficiente");
        }

        cuenta.setMonto(cuenta.getMonto() - monto);
        cuentaRepository.save(cuenta);

        // body a enviar al mock
        Map<String, Object> body = new HashMap<>();
        body.put("amount", monto);

        // llamada real al mock
        Map<String, Object> respuesta = mercadoPagoFeign.procesarPago(body);

        return respuesta;
    }
}
