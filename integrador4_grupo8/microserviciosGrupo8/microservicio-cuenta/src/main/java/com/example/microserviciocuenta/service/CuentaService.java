package com.example.microserviciocuenta.service;

import com.example.microserviciocuenta.entity.Cuenta;
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
    public Cuenta findById(String id) throws Exception {
        return cuentaRepository.findById(Long.valueOf(id)).orElse(null);
    }

    //ejercicio B
    @Transactional
    public Cuenta actualizarEstadoCuenta(Long idUsuario, Boolean cuentaActiva) {
        return cuentaRepository.actualizarEstadoCuenta(idUsuario, cuentaActiva);
    }

}
