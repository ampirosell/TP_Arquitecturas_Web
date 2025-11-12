package com.example.microserviciofacturacion.service;

import com.example.microserviciofacturacion.entity.Factura;
import com.example.microserviciofacturacion.entity.Tarifa;
import com.example.microserviciofacturacion.repository.FacturaRepository;
import com.example.microserviciofacturacion.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
public class FacturacionService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    public Tarifa getTarifaVigente() {
        return tarifaRepository.findFirstByActivaTrue();
    }

    public Tarifa crearTarifa(Tarifa tarifa) {
        tarifaRepository.findAll().forEach(t -> t.setActiva(false)); // desactiva las anteriores
        tarifa.setActiva(true);
        return tarifaRepository.save(tarifa);
    }

    public Factura generarFactura(Long idCuenta, Long idViaje, double km, long minutos, boolean pausaExtendida) {
        Tarifa tarifa = getTarifaVigente();

        double monto = (km * tarifa.getPrecioPorKm()) + (minutos * tarifa.getPrecioPorMinuto());
        if (pausaExtendida)
            monto += tarifa.getRecargoPausa();

        Factura f = new Factura();
        f.setIdCuenta(idCuenta);
        f.setIdViaje(idViaje);
        f.setKmRecorridos(km);
        f.setMinutosTotales(minutos);
        f.setMontoTotal(monto);
        f.setFechaEmision(LocalDateTime.now());
        return facturaRepository.save(f);
    }

    public double totalFacturadoEntre(LocalDate desde, LocalDate hasta) {
        return facturaRepository
                .findByFechaEmisionBetween(desde.atStartOfDay(), hasta.atTime(23,59))
                .stream()
                .mapToDouble(Factura::getMontoTotal)
                .sum();
    }
}
