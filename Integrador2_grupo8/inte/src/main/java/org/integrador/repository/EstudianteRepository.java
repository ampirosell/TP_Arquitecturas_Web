package org.integrador.repository;

import org.integrador.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    // c) Recuperar todos los estudiantes con criterio de ordenamiento
    @Query("SELECT e FROM Estudiante e ORDER BY e.apellido, e.nombre")
    List<Estudiante> findAllOrderedByApellidoAndNombre();

    // d) Recuperar un estudiante por número de libreta universitaria
    @Query("SELECT e FROM Estudiante e WHERE e.numeroLU = :numeroLU")
    Optional<Estudiante> findByNumeroLU(@Param("numeroLU") String numeroLU);

    // e) Recuperar todos los estudiantes por género
    @Query("SELECT e FROM Estudiante e WHERE e.genero = :genero ORDER BY e.apellido, e.nombre")
    List<Estudiante> findByGenero(@Param("genero") String genero);

    // g) Recuperar estudiantes de una carrera específica filtrados por ciudad
    @Query("SELECT DISTINCT e FROM Estudiante e " +
           "JOIN e.carreras ec " +
           "JOIN ec.carrera c " +
           "WHERE c.nombre = :nombreCarrera AND e.ciudadDeResidencia = :ciudad " +
           "ORDER BY e.apellido, e.nombre")
    List<Estudiante> findByCarreraAndCiudad(@Param("nombreCarrera") String nombreCarrera, 
                                          @Param("ciudad") String ciudad);


<<<<<<< Updated upstream
    // Verificar si existe estudiante por DNI
    boolean existsByDni(String dni);
=======
public class EstudianteRepository implements Repository<Estudiante>{
    private EntityManager em;
    public EstudianteRepository(EntityManager em) {
        this.em = em;
    }
    @Override
    public void create(Estudiante object) {
        em.persist(object);
    }

    @Override
    public void update(Estudiante object) {
        //TODO
    }

    @Override
    public void delete(int id) {
        //TODO
    }

    @Override
    public Estudiante findById(long id) {
        return em.find(Estudiante.class, id);
    }
    public Estudiante findByLU(String LU){
        String jpql = "SELECT e FROM Estudiante e WHERE numeroLU = :LU";

        return  em.createQuery(jpql, Estudiante.class).setParameter("LU", LU).getSingleResult();
    }

    public Estudiante findByDNI(String dni){
        String jpql = "SELECT e FROM Estudiante e WHERE dni = :dni";
        return em.createQuery(jpql, Estudiante.class).setParameter("dni", dni).getSingleResult();
    }


    public List<Estudiante> findByGenero(String genero){
        String jpql = "SELECT e FROM Estudiante e WHERE e.genero = :genero";
        Query q = em.createQuery(jpql, Estudiante.class);
        q.setParameter("genero", genero);
        return q.getResultList();
    }

    public List<Estudiante> findByCarreraAndCiudad(int id_carrera,String ciudad) {
        String jpql = "SELECT e FROM Estudiante e JOIN EstudianteDeCarrera ec ON ec.estudiante.dni = e.dni WHERE ec.carrera.carreraId = :carrera AND e.ciudadDeResidencia = :ciudad";
        Query query = em.createQuery(jpql);
        query.setParameter("carrera", id_carrera);
        query.setParameter("ciudad", ciudad);
        return query.getResultList();
    }
    public List<Estudiante> findAll(){
        String jpql = "SELECT e FROM Estudiante e";
        return em.createQuery(jpql, Estudiante.class).getResultList();

}
>>>>>>> Stashed changes

}
