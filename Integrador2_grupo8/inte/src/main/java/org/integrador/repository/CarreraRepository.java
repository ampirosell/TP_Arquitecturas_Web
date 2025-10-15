package org.integrador.repository;

import org.integrador.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    // f) Recuperar carreras con estudiantes inscriptos ordenadas por cantidad de inscriptos
    @Query("SELECT c FROM Carrera c " +
           "WHERE c.id IN (SELECT DISTINCT ec.carrera.id FROM EstudianteDeCarrera ec) " +
           "ORDER BY (SELECT COUNT(ec2) FROM EstudianteDeCarrera ec2 WHERE ec2.carrera.id = c.id) DESC")
    List<Carrera> findCarrerasWithStudentsOrderedByInscripciones();

    // Consulta para obtener carreras con conteo de estudiantes
    @Query("SELECT c, COUNT(ec) as totalInscripciones " +
           "FROM Carrera c LEFT JOIN c.estudiantes ec " +
           "GROUP BY c.id, c.nombre, c.duracion " +
           "ORDER BY totalInscripciones DESC")
    List<Object[]> findCarrerasWithStudentCount();

<<<<<<< Updated upstream
=======
    private EntityManager em;

    public CarreraRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void create(Carrera object) {
        em.persist(object);
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
    public Carrera findById(long id) {
        return em.find(Carrera.class, id);
    }

    public Carrera findByNombre(String nombre) {
        String jpql = "SELECT c FROM Carrera c WHERE c.nombre = :nombre";
        return em.createQuery(jpql, Carrera.class).setParameter("nombre", nombre).getSingleResult();
    }

    @Override
    public List<Carrera> findAll() {
        String jpql = "SELECT c FROM Carrera c";
        return em.createQuery(jpql, Carrera.class).getResultList();
    }

    public List<CarreraDTO> findCarreraConInscriptos() {
        // Primero verificar si hay matrículas
        String countQuery = "SELECT COUNT(ec) FROM EstudianteDeCarrera ec";
        Long totalMatriculas = (Long) em.createQuery(countQuery).getSingleResult();
        System.out.println("Total matrículas en BD: " + totalMatriculas);
        
        if (totalMatriculas == 0) {
            System.out.println("No hay matrículas en la base de datos");
            return new ArrayList<>();
        }
        
        // Consulta más simple usando subconsulta
        String jpql = "SELECT c FROM Carrera c WHERE c.carreraId IN " +
                     "(SELECT DISTINCT ec.carrera.carreraId FROM EstudianteDeCarrera ec)";
        
        List<Carrera> carrerasConEstudiantes = em.createQuery(jpql, Carrera.class).getResultList();
        List<CarreraDTO> carreras = new ArrayList<>();
        
        for (Carrera carrera : carrerasConEstudiantes) {
            // Contar estudiantes para cada carrera
            String countJpql = "SELECT COUNT(ec) FROM EstudianteDeCarrera ec WHERE ec.carrera.carreraId = :carreraId";
            Long cantidad = (Long) em.createQuery(countJpql).setParameter("carreraId", carrera.getCarreraId()).getSingleResult();
            
            carreras.add(new CarreraDTO(carrera, cantidad.intValue()));
        }
        
        // Ordenar por cantidad de estudiantes (descendente)
        carreras.sort((c1, c2) -> Integer.compare(c2.getCantidadInscriptos(), c1.getCantidadInscriptos()));
        
        return carreras;
    }
    public List<ReporteDTO> generarReporte() {
        List<Carrera> carreras = em.createQuery("SELECT c FROM Carrera c ORDER BY c.nombre", Carrera.class).getResultList();
        List<ReporteDTO> reportes = new ArrayList<>();

        for(Carrera carrera : carreras){
            ReporteDTO reporte = new ReporteDTO(carrera.getNombre());

            // Verificar si hay matrículas para esta carrera
            String countMatriculas = "SELECT COUNT(ec) FROM EstudianteDeCarrera ec WHERE ec.carrera.carreraId = :idCarrera";
            Long totalMatriculasCarrera = (Long) em.createQuery(countMatriculas).setParameter("idCarrera", carrera.getCarreraId()).getSingleResult();
            System.out.println("Carrera " + carrera.getNombre() + " tiene " + totalMatriculasCarrera + " matrículas");
            
            if (totalMatriculasCarrera == 0) {
                continue; // Saltar esta carrera si no tiene matrículas
            }
            
            // Consulta JPQL para inscripciones por año
            String jpqlInscriptos = "SELECT YEAR(ec.fechaInscripcion), COUNT(ec) " +
                    "FROM EstudianteDeCarrera ec " +
                    "WHERE ec.carrera.carreraId = :idCarrera " +
                    "GROUP BY YEAR(ec.fechaInscripcion) " +
                    "ORDER BY YEAR(ec.fechaInscripcion)";

            // Consulta JPQL para graduados por año
            String jpqlEgresados = "SELECT YEAR(ec.fechaGraduacion), COUNT(ec) " +
                    "FROM EstudianteDeCarrera ec " +
                    "WHERE ec.carrera.carreraId = :idCarrera AND ec.fechaGraduacion IS NOT NULL " +
                    "GROUP BY YEAR(ec.fechaGraduacion) " +
                    "ORDER BY YEAR(ec.fechaGraduacion)";

            List<Object[]> inscriptosList = em.createQuery(jpqlInscriptos).setParameter("idCarrera", carrera.getCarreraId()).getResultList();
            List<Object[]> egresadosList = em.createQuery(jpqlEgresados).setParameter("idCarrera", carrera.getCarreraId()).getResultList();
            
            System.out.println("Inscripciones encontradas: " + inscriptosList.size());
            System.out.println("Egresados encontrados: " + egresadosList.size());
            
            // Procesar inscripciones
            for (Object[] resultado : inscriptosList) {
                Integer anio = (Integer) resultado[0];
                Long inscriptos = ((Number) resultado[1]).longValue();
                reporte.getInfoPorAnio().put(anio, new CarreraInfoDTO(inscriptos.intValue()));
            }
            
            // Procesar graduados
            for (Object[] resultado : egresadosList) {
                Integer anio = (Integer) resultado[0];
                Long egresados = ((Number) resultado[1]).longValue();
                CarreraInfoDTO info = reporte.getInfoPorAnio().get(anio);
                if (info == null) {
                    info = new CarreraInfoDTO(0);
                    reporte.getInfoPorAnio().put(anio, info);
                }
                info.setEgresados(egresados.intValue());
            }
            reportes.add(reporte);
        }
        return reportes;
    }
>>>>>>> Stashed changes
}
