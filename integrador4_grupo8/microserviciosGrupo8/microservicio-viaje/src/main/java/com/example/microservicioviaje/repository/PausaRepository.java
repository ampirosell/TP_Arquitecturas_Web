package com.example.microservicioviaje.repository;


import com.example.microservicioviaje.entity.Pausa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PausaRepository extends JpaRepository<Pausa, Long> {
}
