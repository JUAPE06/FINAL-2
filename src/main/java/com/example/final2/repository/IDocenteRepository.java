package com.example.final2.repository;

import com.example.final2.model.Docente;

import java.util.List;

public interface IDocenteRepository {
    List<Docente> findAll();
    List<Docente> findByNombre(String query);
    void save(Docente docente);
    void update(Docente docente);
    void delete(int id);
}
