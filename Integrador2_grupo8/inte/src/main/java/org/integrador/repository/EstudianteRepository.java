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


    // Verificar si existe estudiante por DNI
    boolean existsByDni(String dni);

}
