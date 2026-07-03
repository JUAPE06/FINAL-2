package com.example.final2.controller;

import com.example.final2.model.Deuda;
import com.example.final2.model.Docente;
import com.example.final2.service.IDeudaService;
import com.example.final2.service.IDocenteService;
import com.example.final2.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DeudaFormController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private ComboBox<Docente> cboDocente;
    @FXML private TextField txtConcepto;
    @FXML private TextField txtMonto;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cboEstado;

    @FXML private Label lblDocenteError;
    @FXML private Label lblConceptoError;
    @FXML private Label lblMontoError;
    @FXML private Label lblFechaError;
    @FXML private Label lblEstadoError;

    private final IDeudaService deudaService;
    private final IDocenteService docenteService;
    private Deuda deuda;
    private Runnable onGuardar;

    public DeudaFormController(IDeudaService deudaService, IDocenteService docenteService) {
        this.deudaService = deudaService;
        this.docenteService = docenteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cargarDocentes();
        cboEstado.setItems(FXCollections.observableArrayList("Pendiente", "Pagado", "En Proceso"));
        dpFecha.setValue(LocalDate.now());
    }

    private void cargarDocentes() {
        try {
            cboDocente.setItems(FXCollections.observableArrayList(docenteService.listarTodos()));
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo cargar la lista de docentes.");
        }
    }

    public void setDeuda(Deuda deuda) {
        this.deuda = deuda;
        if (deuda != null) {
            lblTitulo.setText("Editar Deuda Administrativa");
            // Buscar y seleccionar el docente en el combo
            for (Docente d : cboDocente.getItems()) {
                if (d.getIdDocente() == deuda.getIdDocente()) {
                    cboDocente.setValue(d);
                    break;
                }
            }
            txtConcepto.setText(deuda.getConcepto());
            txtMonto.setText(String.valueOf(deuda.getMonto()));
            dpFecha.setValue(deuda.getFechaRegistro());
            cboEstado.setValue(deuda.getEstado());
        }
    }

    public void setOnGuardar(Runnable onGuardar) {
        this.onGuardar = onGuardar;
    }

    @FXML
    private void onGuardar() {
        if (!validar()) return;

        try {
            if (deuda == null) deuda = new Deuda();
            
            deuda.setIdDocente(cboDocente.getValue().getIdDocente());
            deuda.setConcepto(txtConcepto.getText().trim());
            deuda.setMonto(Double.parseDouble(txtMonto.getText().trim()));
            deuda.setFechaRegistro(dpFecha.getValue());
            deuda.setEstado(cboEstado.getValue());

            deudaService.guardar(deuda);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo guardar la deuda:", e);
        }
    }

    @FXML
    private void onCancelar() {
        cerrar();
    }

    private void cerrar() {
        Stage stage = (Stage) txtConcepto.getScene().getWindow();
        stage.close();
    }

    private boolean validar() {
        boolean ok = true;
        resetErrores();

        if (cboDocente.getValue() == null) {
            lblDocenteError.setText("Seleccione un docente.");
            lblDocenteError.setVisible(true);
            ok = false;
        }
        if (txtConcepto.getText().isBlank()) {
            lblConceptoError.setText("Ingrese el concepto.");
            lblConceptoError.setVisible(true);
            ok = false;
        }
        try {
            double m = Double.parseDouble(txtMonto.getText());
            if (m < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            lblMontoError.setText("Monto inválido.");
            lblMontoError.setVisible(true);
            ok = false;
        }
        if (dpFecha.getValue() == null) {
            lblFechaError.setText("Seleccione una fecha.");
            lblFechaError.setVisible(true);
            ok = false;
        }
        if (cboEstado.getValue() == null) {
            lblEstadoError.setText("Seleccione el estado.");
            lblEstadoError.setVisible(true);
            ok = false;
        }

        return ok;
    }

    private void resetErrores() {
        lblDocenteError.setVisible(false);
        lblConceptoError.setVisible(false);
        lblMontoError.setVisible(false);
        lblFechaError.setVisible(false);
        lblEstadoError.setVisible(false);
    }
}
