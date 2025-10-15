package org.integrador.repository;


import java.util.List;

public interface Repository <T>{
    void create(T object);
    void update(T object);


    void delete(int id);
    T findById(long id);
    List<T> findAll();
}
