package com.example.microservicioviaje.service;

import com.example.microservicioviaje.entity.Pausa;
import com.example.microservicioviaje.repository.PausaRepository;
import com.example.microservicioviaje.dto.PausaDTO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PausaService{
    private PausaRepository pausaRepository;

    @Autowired
    public PausaService(PausaRepository pr){
        this.pausaRepository=pr;
    }

    @Transactional
    public PausaDTO findById(Long id) {
        return pausaRepository.findById(id).map(PausaDTO::new).orElse(null);
    }

    @Transactional
    public List<PausaDTO> findAll() throws Exception {
        return pausaRepository.findAll().stream().map(PausaDTO::new).collect(Collectors.toList());
    }

    @Transactional
    public PausaDTO save(Pausa entity) throws Exception {
        pausaRepository.save(entity);
        return this.findById(entity.getId());
    }

    @Transactional
    public PausaDTO update(Long id, Pausa updatedPausa) throws Exception {
        return this.save(updatedPausa);
    }
    @Transactional
    public ResponseEntity<String> delete(Long id) throws Exception {
        if (pausaRepository.existsById(id)) {
            pausaRepository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body("Eliminación exitosa");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La entidad con ID " + id + " no existe");
        }
    }
}