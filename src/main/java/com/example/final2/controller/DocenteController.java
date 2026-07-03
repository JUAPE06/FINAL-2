package com.example.final2.controller;

import com.example.final2.config.AppContext;
import com.example.final2.model.Docente;
import com.example.final2.service.IDocenteService;
import com.example.final2.util.AlertUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DocenteController implements Initializable {

    @FXML private TextField txtBusqueda;
    @FXML private Label lblTotal;
    @FXML private TableView<Docente> tableView;
    @FXML private TableColumn<Docente, Integer> colId;
    @FXML private TableColumn<Docente, String> colDni;
    @FXML private TableColumn<Docente, String> colNombres;
    @FXML private TableColumn<Docente, String> colApellidos;
    @FXML private TableColumn<Docente, String> colEspecialidad;
    @FXML private TableColumn<Docente, String> colCondicion;
    @FXML private TableColumn<Docente, Void> colAcciones;

    private final IDocenteService service;
    private final ObservableList<Docente> data = FXCollections.observableArrayList();

    public DocenteController(IDocenteService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        cargarDatos("");
        txtBusqueda.setOnAction(e -> onBuscar());
    }

    private void configurarColumnas() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDocente"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colCondicion.setCellValueFactory(new PropertyValueFactory<>("condicion"));
        colAcciones.setCellFactory(crearCeldaAcciones());
        tableView.setItems(data);
    }

    private Callback<TableColumn<Docente, Void>, TableCell<Docente, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(5, btnEditar, btnEliminar);

            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#D32F2F;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");

                btnEditar.setOnAction(e -> abrirFormulario(getTableView().getItems().get(getIndex())));
                btnEliminar.setOnAction(e -> confirmarEliminar(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String query) {
        try {
            data.setAll(service.buscarPorNombre(query));
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo cargar los docentes:", e);
        }
    }

    @FXML private void onBuscar() { cargarDatos(txtBusqueda.getText()); }
    @FXML private void onCrear() { abrirFormulario(null); }

    private void confirmarEliminar(Docente d) {
        if (!AlertUtil.confirmar("Eliminar", "¿Eliminar a " + d.getApellidos() + "?")) return;
        try {
            service.eliminar(d.getIdDocente());
            onBuscar();
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar el docente (posiblemente tiene deudas asociadas).");
        }
    }

    private void abrirFormulario(Docente d) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/final2/docente-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();
            DocenteFormController ctrl = loader.getController();
            ctrl.setDocente(d);
            ctrl.setOnGuardar(this::onBuscar);
            Stage modal = new Stage();
            modal.setTitle(d == null ? "Nuevo Docente" : "Editar Docente");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "Error al abrir formulario:", e);
        }
    }
}
