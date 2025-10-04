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

}
