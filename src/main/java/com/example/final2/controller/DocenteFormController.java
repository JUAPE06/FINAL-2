package com.example.final2.controller;

import com.example.final2.model.Docente;
import com.example.final2.service.IDocenteService;
import com.example.final2.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DocenteFormController implements Initializable {

    @FXML private Label lblTitulo;
    @FXML private TextField txtDni;
    @FXML private TextField txtNombres;
    @FXML private TextField txtApellidos;
    @FXML private TextField txtEspecialidad;
    @FXML private ComboBox<String> cboCondicion;

    @FXML private Label lblDniError;
    @FXML private Label lblNombresError;
    @FXML private Label lblApellidosError;

    private final IDocenteService service;
    private Docente docente;
    private Runnable onGuardar;

    public DocenteFormController(IDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cboCondicion.setItems(FXCollections.observableArrayList("Nombrado", "Contratado"));
    }

    public void setDocente(Docente d) {
        this.docente = d;
        if (d != null) {
            lblTitulo.setText("Editar Docente");
            txtDni.setText(d.getDni());
            txtNombres.setText(d.getNombres());
            txtApellidos.setText(d.getApellidos());
            txtEspecialidad.setText(d.getEspecialidad());
            cboCondicion.setValue(d.getCondicion());
        }
    }

    public void setOnGuardar(Runnable callback) { this.onGuardar = callback; }

    @FXML
    private void onGuardar() {
        if (!validar()) return;
        try {
            if (docente == null) docente = new Docente();
            docente.setDni(txtDni.getText().trim());
            docente.setNombres(txtNombres.getText().trim());
            docente.setApellidos(txtApellidos.getText().trim());
            docente.setEspecialidad(txtEspecialidad.getText().trim());
            docente.setCondicion(cboCondicion.getValue());
            service.guardar(docente);
            if (onGuardar != null) onGuardar.run();
            cerrar();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtil.error("Error", "No se pudo guardar el docente. Verifique si el DNI ya está registrado.", e);
        }
    }

    @FXML private void onCancelar() { cerrar(); }
    private void cerrar() { ((Stage) txtDni.getScene().getWindow()).close(); }

    private boolean validar() {
        boolean ok = true;
        lblDniError.setVisible(false);
        lblNombresError.setVisible(false);
        lblApellidosError.setVisible(false);

        if (txtDni.getText().length() != 8) {
            lblDniError.setText("DNI debe tener 8 dígitos.");
            lblDniError.setVisible(true);
            ok = false;
        }
        if (txtNombres.getText().isBlank()) {
            lblNombresError.setText("Ingrese nombres.");
            lblNombresError.setVisible(true);
            ok = false;
        }
        if (txtApellidos.getText().isBlank()) {
            lblApellidosError.setText("Ingrese apellidos.");
            lblApellidosError.setVisible(true);
            ok = false;
        }
        return ok;
    }
}
