package org.integrador.service;

import org.integrador.DTO.CarreraDTO;
import org.integrador.DTO.ReporteDTO;
import org.integrador.entity.Carrera;
import org.integrador.repository.CarreraRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class CarreraService {

    private EntityManager em;
    private CarreraRepository cr;

    public CarreraService(EntityManager em){
        this.em = em;
        this.cr = new CarreraRepository(em);
    }

    // Crear nueva carrera
    public void agregarCarrera(Carrera carrera){
        em.getTransaction().begin();
        cr.create(carrera);
        em.getTransaction().commit();
    }

    // Obtener todas las carreras
    public List<Carrera> obtenerTodasLasCarreras(){
        return cr.findAll();
    }

    // Obtener carrera por id
    public Carrera obtenerCarreraPorId(int id){
        return cr.findById(id);
    }

    // f) Obtener carreras con inscriptos ordenadas por cantidad
    public List<CarreraDTO> obtenerCarrerasConInscriptos(){
        return cr.findCarreraConInscriptos();
    }

    // 3) Generar reporte
    public List<ReporteDTO> generarReporte(){
        return cr.generarReporte();
    }
}
