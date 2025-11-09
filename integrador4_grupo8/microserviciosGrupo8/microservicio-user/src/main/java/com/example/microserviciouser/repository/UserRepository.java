package com.example.microserviciouser.repository;

import com.example.microserviciouser.entity.Rol;
import com.example.microserviciouser.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{

    @Query("SELECT u.rol FROM User u WHERE u.id = :id")
    Rol findRolById(Long id);
}
