package com.example.final2.service;

import com.example.final2.model.Deuda;

import java.util.List;

public interface IDeudaService {
    List<Deuda> listarTodas();
    List<Deuda> filtrarDeudas(String concepto, String docente, String estado);
    void guardar(Deuda deuda);
    void eliminar(int id);
    void eliminarSeleccionados(List<Integer> ids);
}
