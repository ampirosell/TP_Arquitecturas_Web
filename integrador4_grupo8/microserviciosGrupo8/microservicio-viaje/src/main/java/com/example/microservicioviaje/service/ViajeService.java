package com.example.microservicioviaje.service;

//import com.example.microserviciomonopatin.entity.Monopatin;
import com.example.microservicioviaje.dto.FinalizarViajeRequest;
import com.example.microservicioviaje.dto.IniciarViajeRequest;
import com.example.microservicioviaje.dto.MonopatinViajesDTO;
import com.example.microservicioviaje.dto.ReporteUsoMonopatinDTO;
import com.example.microservicioviaje.entity.Viaje;
import com.example.microservicioviaje.model.CuentaRemote;
import com.example.microservicioviaje.model.MonopatinRemote;
import com.example.microservicioviaje.model.ParadaRemote;
import com.example.microservicioviaje.model.enums.EstadoMonopatinRemote;
import com.example.microservicioviaje.repository.PausaRepository;
import com.example.microservicioviaje.repository.ViajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;

    @Autowired
    private PausaRepository pausaRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${services.cuenta.base-url}")
    private String cuentaServiceBaseUrl;

    @Value("${services.monopatin.base-url}")
    private String monopatinServiceBaseUrl;

    @Value("${services.parada.base-url}")
    private String paradaServiceBaseUrl;

    @Value("${services.facturacion.base-url}")
    private String facturacionServiceBaseUrl;

    @Value("${viaje.parada.radio-validacion-metros:50}")
    private double radioValidacionParadaMetros;

    @Transactional
    public List<Viaje> getAll() {
        return viajeRepository.findAll();
    }

    @Transactional
    public Viaje save(Viaje viaje) {
        Viaje viajeNew;
        viajeNew = viajeRepository.save(viaje);
        return viajeNew;
    }

    @Transactional
    public void delete(Viaje viaje) {

        viajeRepository.delete(viaje);
    }

    @Transactional
    public Viaje update(Viaje viaje) {
        return viajeRepository.save(viaje);
    }

    @Transactional
    public Viaje findById(Long id) throws Exception {
        return viajeRepository.findById(id).orElse(null);
    }

    /*
    *\e. Como administrador quiero ver los usuarios que más utilizan los monopatines, filtrado por
período y por tipo de usuario.

     */
    public List<Long> obtenerUsuariosConMasViajesPorPeriodo(LocalDateTime desde, LocalDateTime hasta) {
        return viajeRepository.findUsuariosConMasViajesPorPeriodo(desde, hasta);
    }

   /* c. Como administrador quiero consultar los monopatines con más de X viajes en un cierto año.*/

    @Transactional(readOnly = true)
    public List<Long> obtenerMonopatinesConMasViajes(int anio, long minViajes) {
        return viajeRepository.findMonopatinesConMasViajes(anio, minViajes);
    }

    @Transactional(readOnly = true)
    public List<ReporteUsoMonopatinDTO> generarReporteUso(LocalDate desde, LocalDate hasta) {
        LocalDate inicio = desde != null ? desde : LocalDate.of(1970, 1, 1);
        LocalDate fin = hasta != null ? hasta : LocalDate.now();

        LocalDateTime fechaDesde = inicio.atStartOfDay();
        LocalDateTime fechaHasta = fin.atTime(23, 59, 59);

        List<Viaje> viajes = viajeRepository.findByFechaInicioBetween(fechaDesde, fechaHasta);

        Map<Long, ResumenUsoMonopatin> acumulado = new HashMap<>();

        for (Viaje viaje : viajes) {
            if (viaje.getIdMonopatin() == null || viaje.getFechaInicio() == null || viaje.getFechaFin() == null) {
                continue;
            }

            long minutosTotales = Duration.between(viaje.getFechaInicio(), viaje.getFechaFin()).toMinutes();
            if (minutosTotales < 0) {
                continue;
            }

            Long minutosPausa = pausaRepository.obtenerMinutosPausaPorViaje(viaje.getIdViaje());
            long pausa = minutosPausa != null ? minutosPausa : 0L;

            ResumenUsoMonopatin resumen = acumulado.computeIfAbsent(viaje.getIdMonopatin(), key -> new ResumenUsoMonopatin());
            resumen.kilometros += viaje.getKmRecorridos();
            resumen.minutosTotales += minutosTotales;
            resumen.minutosPausa += pausa;
        }

        return acumulado.entrySet().stream()
                .map(entry -> {
                    ResumenUsoMonopatin resumen = entry.getValue();
                    long minutosConPausa = resumen.minutosTotales;
                    long minutosSinPausa = Math.max(0, resumen.minutosTotales - resumen.minutosPausa);

                    return ReporteUsoMonopatinDTO.builder()
                            .idMonopatin(entry.getKey())
                            .kilometros(resumen.kilometros)
                            .minutosConPausa(minutosConPausa)
                            .minutosPausa(resumen.minutosPausa)
                            .minutosSinPausa(minutosSinPausa)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Viaje iniciarViaje(IniciarViajeRequest request) {
        CuentaRemote cuenta = obtenerCuenta(request.getIdCuenta());
        if (cuenta == null) {
            throw new IllegalStateException("La cuenta indicada no existe");
        }
        if (!cuenta.isCuentaActiva()) {
            throw new IllegalStateException("La cuenta no está activa");
        }
        if (cuenta.getMonto() == null || cuenta.getMonto() <= 0) {
            throw new IllegalStateException("La cuenta no posee saldo disponible");
        }

        MonopatinRemote monopatin = obtenerMonopatin(request.getIdMonopatin());
        if (monopatin == null) {
            throw new IllegalStateException("El monopatín indicado no existe");
        }
        if (monopatin.getEstadoMonopatin() != EstadoMonopatinRemote.LIBRE) {
            throw new IllegalStateException("El monopatín no está disponible");
        }

        Viaje viaje = new Viaje();
        viaje.setFechaInicio(LocalDateTime.now());
        viaje.setKmRecorridos(0);
        viaje.setPausa(false);
        viaje.setMonopatin(request.getIdMonopatin());
        viaje.setIdUsuario(request.getIdUsuario());
        viaje.setIdCuenta(request.getIdCuenta());

        Viaje guardado = viajeRepository.save(viaje);

        Map<String, Object> bodyEstado = new HashMap<>();
        bodyEstado.put("estado", "EN_USO");
        bodyEstado.put("viajeId", guardado.getIdViaje());
        ejecutarPatch(monopatinServiceBaseUrl + "/" + request.getIdMonopatin() + "/estado", bodyEstado);

        return guardado;
    }

    @Transactional
    public Viaje finalizarViaje(Long idViaje, FinalizarViajeRequest request) {
        Viaje viaje = viajeRepository.findById(idViaje)
                .orElseThrow(() -> new IllegalStateException("Viaje no encontrado"));

        if (viaje.getFechaFin() != null) {
            throw new IllegalStateException("El viaje ya está finalizado");
        }

        MonopatinRemote monopatin = obtenerMonopatin(viaje.getMonopatin());
        if (monopatin == null) {
            throw new IllegalStateException("No se encontró el monopatín asociado al viaje");
        }

        ParadaRemote parada = obtenerParada(request.getParadaId());
        if (parada == null) {
            throw new IllegalStateException("La parada indicada no existe");
        }

        validarUbicacionEnParada(parada, request.getLatitud(), request.getLongitud());

        LocalDateTime fechaFin = LocalDateTime.now();
        viaje.setFechaFin(fechaFin);
        viaje.setKmRecorridos(request.getKilometrosRecorridos());
        Long minutosPausa = pausaRepository.obtenerMinutosPausaPorViaje(idViaje);
        long pausaTotal = minutosPausa != null ? minutosPausa : 0L;
        boolean pausaExtendida = pausaTotal > 15;
        viaje.setPausa(pausaTotal > 0);

        Viaje actualizado = viajeRepository.save(viaje);

        long minutosTotales = Math.max(0, Duration.between(viaje.getFechaInicio(), fechaFin).toMinutes());

        Map<String, Object> bodyEstadoLibre = new HashMap<>();
        bodyEstadoLibre.put("estado", "LIBRE");
        bodyEstadoLibre.put("viajeId", null);
        ejecutarPatch(monopatinServiceBaseUrl + "/" + monopatin.getIdMonopatin() + "/estado", bodyEstadoLibre);

        Map<String, Object> bodyUbicacion = new HashMap<>();
        bodyUbicacion.put("latitud", request.getLatitud());
        bodyUbicacion.put("longitud", request.getLongitud());
        bodyUbicacion.put("paradaId", request.getParadaId());
        ejecutarPatch(monopatinServiceBaseUrl + "/" + monopatin.getIdMonopatin() + "/ubicacion", bodyUbicacion);

        Map<String, Object> bodyKilometros = new HashMap<>();
        bodyKilometros.put("kilometrosRecorridos", request.getKilometrosRecorridos());
        ejecutarPatch(monopatinServiceBaseUrl + "/" + monopatin.getIdMonopatin() + "/kilometros", bodyKilometros);

        generarFactura(viaje.getIdCuenta(), viaje.getIdViaje(), request.getKilometrosRecorridos(), minutosTotales, pausaExtendida);

        return actualizado;
    }

    private static class ResumenUsoMonopatin {
        private double kilometros = 0;
        private long minutosTotales = 0;
        private long minutosPausa = 0;
    }

    private CuentaRemote obtenerCuenta(Long idCuenta) {
        try {
            return restTemplate.getForObject(cuentaServiceBaseUrl + "/" + idCuenta, CuentaRemote.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo consultar la cuenta", e);
        }
    }

    private MonopatinRemote obtenerMonopatin(Long idMonopatin) {
        try {
            return restTemplate.getForObject(monopatinServiceBaseUrl + "/" + idMonopatin, MonopatinRemote.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo consultar el monopatín", e);
        }
    }

    private ParadaRemote obtenerParada(Long idParada) {
        try {
            return restTemplate.getForObject(paradaServiceBaseUrl + "/" + idParada, ParadaRemote.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo consultar la parada", e);
        }
    }

    private void generarFactura(Long idCuenta, Long idViaje, Double km, long minutos, boolean pausaExtendida) {
        String url = String.format(Locale.US,
                "%s/generar?idCuenta=%d&idViaje=%d&km=%.2f&minutos=%d&pausaExtendida=%s",
                facturacionServiceBaseUrl, idCuenta, idViaje, km, minutos, pausaExtendida);
        try {
            restTemplate.postForObject(url, null, Object.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo generar la factura del viaje", e);
        }
    }

    private void ejecutarPatch(String url, Map<String, Object> body) {
        try {
            restTemplate.patchForObject(url, body, Void.class);
        } catch (RestClientException e) {
            throw new IllegalStateException("No se pudo actualizar el estado remoto", e);
        }
    }

    private void validarUbicacionEnParada(ParadaRemote parada, Double latitud, Double longitud) {
        if (parada.getLatitud() == null || parada.getLongitud() == null) {
            throw new IllegalStateException("La parada indicada no tiene coordenadas registradas");
        }
        if (latitud == null || longitud == null) {
            throw new IllegalStateException("Debe informar la ubicación actual del monopatín");
        }
        double distancia = calcularDistanciaMetros(parada.getLatitud(), parada.getLongitud(), latitud, longitud);
        if (distancia > radioValidacionParadaMetros) {
            throw new IllegalStateException(String.format(
                    Locale.US,
                    "El monopatín se encuentra a %.2f metros de la parada seleccionada. Debe estar dentro de %.0f metros para finalizar el viaje",
                    distancia, radioValidacionParadaMetros));
        }
    }

    private double calcularDistanciaMetros(double lat1, double lon1, double lat2, double lon2) {
        double radioTierra = 6371000; // metros
        double latRad1 = Math.toRadians(lat1);
        double latRad2 = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(latRad1) * Math.cos(latRad2) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return BigDecimal.valueOf(radioTierra * c)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
    //EJERCICIO H
    @Transactional(readOnly = true)
    public ReporteUsoMonopatinDTO obtenerUsoPorUsuario(Long idUsuario, Long idCuenta, LocalDate desde, LocalDate hasta) {
        LocalDateTime fechaDesde = desde.atStartOfDay();
        LocalDateTime fechaHasta = hasta.atTime(23, 59, 59);

        List<Viaje> viajes;

        if (idCuenta != null) {
            // Busca todos los viajes de los usuarios de esa cuenta
            viajes = viajeRepository.findByCuentaAndFechaInicioBetween(idCuenta, fechaDesde, fechaHasta);
        } else {
            // Solo los del usuario indicado
            viajes = viajeRepository.findByUsuarioAndFechaInicioBetween(idUsuario, fechaDesde, fechaHasta);
        }

        if (viajes.isEmpty()) return null;

        double km = viajes.stream().mapToDouble(Viaje::getKmRecorridos).sum();
        long minutos = viajes.stream()
                .mapToLong(v -> Duration.between(v.getFechaInicio(), v.getFechaFin()).toMinutes())
                .sum();

        return new ReporteUsoMonopatinDTO(null, km, minutos, 0, minutos); // o podés crear otro DTO más específico
    }

}
