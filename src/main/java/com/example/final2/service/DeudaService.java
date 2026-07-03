package com.example.final2.service;

import com.example.final2.model.Deuda;
import com.example.final2.repository.IDeudaRepository;

import java.util.List;

public class DeudaService implements IDeudaService {

    private final IDeudaRepository repository;

    public DeudaService(IDeudaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Deuda> listarTodas() {
        return repository.findAll();
    }

    @Override
    public List<Deuda> filtrarDeudas(String concepto, String docente, String estado) {
        return repository.findByFilters(concepto, docente, estado);
    }

    @Override
    public void guardar(Deuda d) {
        if (d.getIdDeuda() > 0) {
            repository.update(d);
        } else {
            repository.save(d);
        }
    }

    @Override
    public void eliminar(int id) {
        repository.delete(id);
    }

    @Override
    public void eliminarSeleccionados(List<Integer> ids) {
        repository.deleteAll(ids);
    }
}
