package com.example.final2.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Docente {
    private final IntegerProperty idDocente = new SimpleIntegerProperty();
    private final StringProperty dni = new SimpleStringProperty();
    private final StringProperty nombres = new SimpleStringProperty();
    private final StringProperty apellidos = new SimpleStringProperty();
    private final StringProperty especialidad = new SimpleStringProperty();
    private final StringProperty condicion = new SimpleStringProperty();

    public Docente() {}

    public Docente(int idDocente, String dni, String nombres, String apellidos, String especialidad, String condicion) {
        setIdDocente(idDocente);
        setDni(dni);
        setNombres(nombres);
        setApellidos(apellidos);
        setEspecialidad(especialidad);
        setCondicion(condicion);
    }

    public IntegerProperty idDocenteProperty() { return idDocente; }
    public StringProperty dniProperty() { return dni; }
    public StringProperty nombresProperty() { return nombres; }
    public StringProperty apellidosProperty() { return apellidos; }
    public StringProperty especialidadProperty() { return especialidad; }
    public StringProperty condicionProperty() { return condicion; }

    public int getIdDocente() { return idDocente.get(); }
    public String getDni() { return dni.get(); }
    public String getNombres() { return nombres.get(); }
    public String getApellidos() { return apellidos.get(); }
    public String getEspecialidad() { return especialidad.get(); }
    public String getCondicion() { return condicion.get(); }

    public void setIdDocente(int value) { idDocente.set(value); }
    public void setDni(String value) { dni.set(value); }
    public void setNombres(String value) { nombres.set(value); }
    public void setApellidos(String value) { apellidos.set(value); }
    public void setEspecialidad(String value) { especialidad.set(value); }
    public void setCondicion(String value) { condicion.set(value); }

    @Override
    public String toString() {
        return getApellidos() + ", " + getNombres();
    }
}
