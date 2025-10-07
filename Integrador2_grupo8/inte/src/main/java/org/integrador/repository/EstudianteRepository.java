package org.integrador.repository;

import org.integrador.entity.Estudiante;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;


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

}
