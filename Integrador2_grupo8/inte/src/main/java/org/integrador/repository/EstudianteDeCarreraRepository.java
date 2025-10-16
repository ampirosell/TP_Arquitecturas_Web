package org.integrador.repository;


import org.integrador.entity.EstudianteDeCarrera;
import jakarta.persistence.EntityManager;
import java.util.List;


public class EstudianteDeCarreraRepository implements Repository<EstudianteDeCarrera> {

        private EntityManager em;
        public EstudianteDeCarreraRepository(EntityManager em) {
            this.em = em;
        }
        @Override
        public void create(EstudianteDeCarrera object) {
            em.persist(object);
        }

        @Override
        public void update(EstudianteDeCarrera object) {
            //TODO
        }

        @Override
        public void delete(int id) {
            //TODO
        }

        @Override
        public EstudianteDeCarrera findById(int id) {
            EstudianteDeCarrera e = em.find(EstudianteDeCarrera.class, id);
            return e;

        }

    @Override
    public List<EstudianteDeCarrera> findAll() {
        String jpql = "SELECT ec FROM EstudianteDeCarrera ec";
        return em.createQuery(jpql, EstudianteDeCarrera.class).getResultList();
    }

    // Método que reemplaza existsById
        public boolean existe(long dniEstudiante, long idCarrera){
            String jpql = "SELECT COUNT(ec) FROM EstudianteDeCarrera ec " +
                    "WHERE ec.estudiante.dni = :dni AND ec.carrera.carreraId = :idCarrera";
            Long count = em.createQuery(jpql, Long.class)
                    .setParameter("dni", dniEstudiante)
                    .setParameter("idCarrera", idCarrera)
                    .getSingleResult();
            return count > 0;
    }


}

