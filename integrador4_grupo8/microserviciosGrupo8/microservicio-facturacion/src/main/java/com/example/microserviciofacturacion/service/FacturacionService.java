package com.example.microserviciofacturacion.service;

import com.example.microserviciofacturacion.entity.Factura;
import com.example.microserviciofacturacion.entity.Tarifa;
import com.example.microserviciofacturacion.repository.FacturaRepository;
import com.example.microserviciofacturacion.repository.TarifaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
public class FacturacionService {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private TarifaRepository tarifaRepository;

    @Transactional
    public Tarifa getTarifaVigente() {
        activarTarifasVigentes();
        return tarifaRepository.findTopByFechaInicioLessThanEqualOrderByFechaInicioDesc(LocalDate.now())
                .orElseThrow(() -> new IllegalStateException("No existe una tarifa vigente"));
    }

    @Transactional
    public Tarifa crearTarifa(Tarifa tarifa) {
        if (tarifa.getFechaInicio() == null) {
            tarifa.setFechaInicio(LocalDate.now());
        }
        boolean esVigente = !tarifa.getFechaInicio().isAfter(LocalDate.now());
        tarifa.setActiva(esVigente);
        Tarifa guardada = tarifaRepository.save(tarifa);
        if (esVigente) {
            tarifaRepository.desactivarOtrasTarifas(guardada.getId());
        }
        return guardada;
    }

    private void activarTarifasVigentes() {
        LocalDate hoy = LocalDate.now();
        tarifaRepository.activarTarifasVigentes(hoy);
        tarifaRepository.findTopByFechaInicioLessThanEqualOrderByFechaInicioDesc(hoy)
                .ifPresent(t -> tarifaRepository.desactivarOtrasTarifas(t.getId()));
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public double totalFacturadoEnMeses(int anio, int mesInicio, int mesFin) {
        if (mesInicio < 1 || mesInicio > 12 || mesFin < 1 || mesFin > 12 || mesInicio > mesFin) {
            throw new IllegalArgumentException("Rango de meses inválido");
        }
        YearMonth inicio = YearMonth.of(anio, mesInicio);
        YearMonth fin = YearMonth.of(anio, mesFin);

        LocalDate desde = inicio.atDay(1);
        LocalDate hasta = fin.atEndOfMonth();
        return totalFacturadoEntre(desde, hasta);
    }
}
