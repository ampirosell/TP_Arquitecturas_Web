package org.integrador.repository;

import org.integrador.DTO.CarreraConEstudiantesDTO;
import org.integrador.entity.Carrera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Long> {

    // f) Recuperar carreras con estudiantes inscriptos ordenadas por cantidad
    @Query("SELECT new org.integrador.DTO.CarreraConEstudiantesDTO(c.id, c.nombre, c.duracion, COUNT(ec)) " +
           "FROM Carrera c LEFT JOIN c.estudiantes ec " +
           "GROUP BY c.id, c.nombre, c.duracion " +
           "HAVING COUNT(ec) > 0 " +
           "ORDER BY COUNT(ec) DESC")
    List<CarreraConEstudiantesDTO> findCarrerasWithStudentCountOrdered();

    // Buscar carrera por nombre
    Optional<Carrera> findByNombre(String nombre);

    // Verificar si existe carrera por nombre
    boolean existsByNombre(String nombre);
}
