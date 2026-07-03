package com.example.final2.service;

import com.example.final2.model.Docente;
import com.example.final2.repository.IDocenteRepository;

import java.util.List;

public class DocenteService implements IDocenteService {

    private final IDocenteRepository repository;

    public DocenteService(IDocenteRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Docente> listarTodos() {
        return repository.findAll();
    }

    @Override
    public List<Docente> buscarPorNombre(String query) {
        return repository.findByNombre(query);
    }

    @Override
    public void guardar(Docente d) {
        if (d.getIdDocente() > 0) {
            repository.update(d);
        } else {
            repository.save(d);
        }
    }

    @Override
    public void eliminar(int id) {
        repository.delete(id);
    }
}
