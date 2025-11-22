package com.example.microserviciocuenta.service;

import com.example.microserviciocuenta.entity.Cuenta;
import com.example.microserviciocuenta.entity.TipoCuenta;
import com.example.microserviciocuenta.repository.CuentaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    @Autowired
    CuentaRepository cuentaRepository;


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

    //ejercicio B
    @Transactional
    public Cuenta actualizarEstadoCuenta(Long idUsuario, Boolean cuentaActiva) {
        int updated = cuentaRepository.actualizarEstadoCuenta(idUsuario, cuentaActiva);
        return updated > 0 ? cuentaRepository.findByIdUsuario(idUsuario).orElse(null) : null;
    }

    @Transactional
    public Cuenta actualizarEstadoCuentaPorId(Long idCuenta, Boolean cuentaActiva) {
        int updated = cuentaRepository.actualizarEstadoCuentaPorId(idCuenta, cuentaActiva);
        return updated > 0 ? cuentaRepository.findById(idCuenta).orElse(null) : null;
    }

    public boolean esPremium(Long idUsuario) {

        Cuenta cuenta = cuentaRepository.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        return cuenta.isCuentaActiva() && cuenta.getTipo() == TipoCuenta.PREMIUM;
    }
}
