package com.example.final2.service;

import com.example.final2.model.Docente;

import java.util.List;

public interface IDocenteService {
    List<Docente> listarTodos();
    List<Docente> buscarPorNombre(String query);
    void guardar(Docente docente);
    void eliminar(int id);
}
