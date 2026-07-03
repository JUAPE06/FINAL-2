package com.example.final2.repository;

import com.example.final2.model.Deuda;

import java.util.List;

public interface IDeudaRepository {
    List<Deuda> findAll();
    List<Deuda> findByFilters(String concepto, String docente, String estado);
    void save(Deuda deuda);
    void update(Deuda deuda);
    void delete(int id);
    void deleteAll(List<Integer> ids);
}
