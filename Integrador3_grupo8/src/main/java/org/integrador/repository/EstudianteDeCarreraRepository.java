package org.integrador.repository;

import org.integrador.entity.EstudianteDeCarrera;
import org.integrador.entity.EstudianteCarreraId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteDeCarreraRepository extends JpaRepository<EstudianteDeCarrera, EstudianteCarreraId> {

    // Consultas para el reporte de carreras por año
    @Query("SELECT YEAR(ec.fechaInscripcion) as año, " +
           "COUNT(CASE WHEN ec.graduado = false THEN 1 END) as inscriptos, " +
           "COUNT(CASE WHEN ec.graduado = true THEN 1 END) as egresados " +
           "FROM EstudianteDeCarrera ec " +
           "JOIN ec.carrera c " +
           "WHERE c.nombre = :nombreCarrera " +
           "GROUP BY YEAR(ec.fechaInscripcion) " +
           "ORDER BY YEAR(ec.fechaInscripcion)")
    List<Object[]> findReporteCarreraPorAño(@Param("nombreCarrera") String nombreCarrera);

    // Reporte general de todas las carreras por año
    @Query("SELECT c.nombre, YEAR(ec.fechaInscripcion) as año, " +
           "COUNT(CASE WHEN ec.graduado = false THEN 1 END) as inscriptos, " +
           "COUNT(CASE WHEN ec.graduado = true THEN 1 END) as egresados " +
           "FROM EstudianteDeCarrera ec " +
           "JOIN ec.carrera c " +
           "GROUP BY c.nombre, YEAR(ec.fechaInscripcion) " +
           "ORDER BY c.nombre, YEAR(ec.fechaInscripcion)")
    List<Object[]> findReporteGeneralCarrerasPorAño();

    // Buscar inscripciones por estudiante y carrera
    @Query("SELECT ec FROM EstudianteDeCarrera ec " +
           "WHERE ec.estudiante.id = :estudianteId AND ec.carrera.id = :carreraId")
    List<EstudianteDeCarrera> findByEstudianteAndCarrera(@Param("estudianteId") Long estudianteId, 
                                                        @Param("carreraId") Long carreraId);

    // Contar estudiantes por carrera
    @Query("SELECT COUNT(ec) FROM EstudianteDeCarrera ec WHERE ec.carrera.id = :carreraId")
    Long countByCarrera(@Param("carreraId") Long carreraId);

    // Contar estudiantes graduados por carrera
    @Query("SELECT COUNT(ec) FROM EstudianteDeCarrera ec " +
           "WHERE ec.carrera.id = :carreraId AND ec.graduado = true")
    Long countGraduadosByCarrera(@Param("carreraId") Long carreraId);

    // Contar estudiantes activos por carrera
    @Query("SELECT COUNT(ec) FROM EstudianteDeCarrera ec " +
           "WHERE ec.carrera.id = :carreraId AND ec.graduado = false")
    Long countActivosByCarrera(@Param("carreraId") Long carreraId);
}
