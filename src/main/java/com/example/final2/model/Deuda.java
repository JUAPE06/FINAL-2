package com.example.final2.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Deuda {
    private final IntegerProperty idDeuda = new SimpleIntegerProperty();
    private final IntegerProperty idDocente = new SimpleIntegerProperty();
    private final StringProperty concepto = new SimpleStringProperty();
    private final DoubleProperty monto = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> fechaRegistro = new SimpleObjectProperty<>();
    private final StringProperty estado = new SimpleStringProperty();
    private final StringProperty docenteNombre = new SimpleStringProperty();
    private final BooleanProperty seleccionado = new SimpleBooleanProperty(false);

    public Deuda() {}

    public Deuda(int idDeuda, int idDocente, String concepto, double monto, LocalDate fechaRegistro, String estado, String docenteNombre) {
        setIdDeuda(idDeuda);
        setIdDocente(idDocente);
        setConcepto(concepto);
        setMonto(monto);
        setFechaRegistro(fechaRegistro);
        setEstado(estado);
        setDocenteNombre(docenteNombre);
    }

    public IntegerProperty idDeudaProperty() { return idDeuda; }
    public IntegerProperty idDocenteProperty() { return idDocente; }
    public StringProperty conceptoProperty() { return concepto; }
    public DoubleProperty montoProperty() { return monto; }
    public ObjectProperty<LocalDate> fechaRegistroProperty() { return fechaRegistro; }
    public StringProperty estadoProperty() { return estado; }
    public StringProperty docenteNombreProperty() { return docenteNombre; }
    public BooleanProperty seleccionadoProperty() { return seleccionado; }

    public int getIdDeuda() { return idDeuda.get(); }
    public int getIdDocente() { return idDocente.get(); }
    public String getConcepto() { return concepto.get(); }
    public double getMonto() { return monto.get(); }
    public LocalDate getFechaRegistro() { return fechaRegistro.get(); }
    public String getEstado() { return estado.get(); }
    public String getDocenteNombre() { return docenteNombre.get(); }
    public boolean isSeleccionado() { return seleccionado.get(); }

    public void setIdDeuda(int value) { idDeuda.set(value); }
    public void setIdDocente(int value) { idDocente.set(value); }
    public void setConcepto(String value) { concepto.set(value); }
    public void setMonto(double value) { monto.set(value); }
    public void setFechaRegistro(LocalDate value) { fechaRegistro.set(value); }
    public void setEstado(String value) { estado.set(value); }
    public void setDocenteNombre(String value) { docenteNombre.set(value); }
    public void setSeleccionado(boolean value) { seleccionado.set(value); }
}
