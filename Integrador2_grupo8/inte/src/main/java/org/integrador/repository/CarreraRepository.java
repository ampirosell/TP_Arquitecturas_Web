package org.integrador.repository;

import org.integrador.DTO.CarreraDTO;
import org.integrador.DTO.CarreraInfoDTO;
import org.integrador.DTO.ReporteDTO;
import org.integrador.entity.Carrera;


import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class CarreraRepository implements Repository<Carrera> {

    private EntityManager em;

    public CarreraRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void create(Carrera object) {
        em.merge(object);
    }

    @Override
    public void update(Carrera object) {
        //TODO
    }


    @Override
    public void delete(int id) {
        //TODO
    }

    @Override
    public Carrera findById(int id) {
        return em.find(Carrera.class, id);
    }

    @Override
    public List<Carrera> findAll() {
        String jpql = "SELECT c FROM Carrera c";
        return em.createQuery(jpql, Carrera.class).getResultList();
    }

    public List<CarreraDTO> findCarreraConInscriptos() {
        String jpql = "SELECT c, SIZE(c.estudiantes) FROM Carrera c WHERE SIZE(c.estudiantes) > 0 ORDER BY SIZE(c.estudiantes) DESC ";

        List<Object[]> resultados = em.createQuery(jpql, Object[].class).getResultList();
        List<CarreraDTO> carreras = new ArrayList<>();

        for (Object[] r : resultados) {
            Carrera carrera = (Carrera) r[0];
            Long cantidad = ((Number) r[1]).longValue(); // <-- más seguro que Integer
            carreras.add(new CarreraDTO(carrera, cantidad.intValue()));
        }
        return carreras;
    }
    public List<ReporteDTO> generarReporte() {
        List<Carrera> carreras = em.createQuery("SELECT c FROM Carrera c ORDER BY c.nombre", Carrera.class).getResultList();
        List<ReporteDTO> reportes = new ArrayList<>();

        for(Carrera carrera : carreras){
            ReporteDTO reporte = new ReporteDTO(carrera.getNombre());

            String jpql = "SELECT ec.fechaInscripcion, COUNT(ec) " +
                    "FROM EstudianteDeCarrera ec " +
                    "WHERE ec.carrera.id = :idCarrera " +
                    "GROUP BY ec.fechaInscripcion " +
                    "ORDER BY ec.fechaInscripcion";

            String jpql2 = "SELECT ec.fechaGraduacion, COUNT(ec) " +
                    "FROM EstudianteDeCarrera ec " +
                    "WHERE ec.carrera.id = :idCarrera AND ec.fechaGraduacion IS NOT NULL " +
                    "GROUP BY ec.fechaGraduacion " +
                    "ORDER BY ec.fechaGraduacion";

            List<Object[]> inscriptosList = em.createQuery(jpql).setParameter("idCarrera", carrera.getId()).getResultList();
            List<Object[]> esgresadosList = em.createQuery(jpql2).setParameter("idCarrera", carrera.getId()).getResultList();
            for (Object[] resultado : inscriptosList){
                Date fecha = (Date) resultado[0];
                int anio = fecha.getYear() + 1900; // Convert Date to year
                int inscriptos = ((Number) resultado[1]).intValue();
                reporte.getInfoPorAnio().put(anio, new CarreraInfoDTO(inscriptos));
            }
            for (Object[] resultado : esgresadosList){
                Date fecha = (Date) resultado[0];
                int anio = fecha.getYear() + 1900; // Convert Date to year
                int egresados = ((Number) resultado[1]).intValue();
                CarreraInfoDTO c = reporte.getInfoPorAnio().get(anio);
                if (c == null){
                    c = new CarreraInfoDTO(0);
                }
                c.setEgresados(egresados);
                reporte.getInfoPorAnio().put(anio, c);
            }
            reportes.add(reporte);
        }
        return reportes;
    }
}
