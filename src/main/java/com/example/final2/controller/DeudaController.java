package com.example.final2.controller;

import com.example.final2.config.AppContext;
import com.example.final2.model.Deuda;
import com.example.final2.service.IDeudaService;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DeudaController implements Initializable {

    @FXML private TextField txtDocente;
    @FXML private TextField txtConcepto;
    @FXML private ComboBox<String> cboEstadoFiltro;
    @FXML private Label lblTotal;

    @FXML private TableView<Deuda> tableView;
    @FXML private TableColumn<Deuda, Boolean> colSeleccion;
    @FXML private TableColumn<Deuda, Integer> colId;
    @FXML private TableColumn<Deuda, String> colDocente;
    @FXML private TableColumn<Deuda, String> colConcepto;
    @FXML private TableColumn<Deuda, Double> colMonto;
    @FXML private TableColumn<Deuda, java.time.LocalDate> colFecha;
    @FXML private TableColumn<Deuda, String> colEstado;
    @FXML private TableColumn<Deuda, Void> colAcciones;

    private final IDeudaService service;
    private final ObservableList<Deuda> data = FXCollections.observableArrayList();

    public DeudaController(IDeudaService service) {
        this.service = service;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarColumnas();
        configurarFiltros();
        cargarDatos("", "", "");

        txtDocente.setOnAction(e -> onBuscar());
        txtConcepto.setOnAction(e -> onBuscar());
    }

    private void configurarFiltros() {
        cboEstadoFiltro.setItems(FXCollections.observableArrayList("", "Pendiente", "Pagado", "En Proceso"));
        cboEstadoFiltro.getSelectionModel().select(0);
    }

    private void configurarColumnas() {
        tableView.setEditable(true);

        colSeleccion.setCellValueFactory(cell -> cell.getValue().seleccionadoProperty());
        colSeleccion.setCellFactory(CheckBoxTableCell.forTableColumn(colSeleccion));
        colSeleccion.setEditable(true);

        colId.setCellValueFactory(new PropertyValueFactory<>("idDeuda"));
        colDocente.setCellValueFactory(new PropertyValueFactory<>("docenteNombre"));
        colConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaRegistro"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colAcciones.setCellFactory(crearCeldaAcciones());

        tableView.setItems(data);
    }

    private Callback<TableColumn<Deuda, Void>, TableCell<Deuda, Void>> crearCeldaAcciones() {
        return col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");
            private final HBox hbox = new HBox(5, btnEditar, btnEliminar);

            {
                hbox.setAlignment(Pos.CENTER);
                btnEditar.setStyle("-fx-background-color:#1976D2;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");
                btnEliminar.setStyle("-fx-background-color:#D32F2F;-fx-text-fill:white;-fx-cursor:hand;-fx-font-size:11px;");

                btnEditar.setOnAction(e -> {
                    Deuda deuda = getTableView().getItems().get(getIndex());
                    abrirFormulario(deuda);
                });
                btnEliminar.setOnAction(e -> {
                    Deuda deuda = getTableView().getItems().get(getIndex());
                    confirmarEliminar(deuda);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        };
    }

    private void cargarDatos(String concepto, String docente, String estado) {
        try {
            List<Deuda> lista = service.filtrarDeudas(concepto, docente, estado);
            data.setAll(lista);
            lblTotal.setText("Total: " + data.size() + " registro(s)");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo cargar las deudas:", e);
        }
    }

    @FXML
    private void onBuscar() {
        String estado = cboEstadoFiltro.getValue() == null ? "" : cboEstadoFiltro.getValue();
        cargarDatos(txtConcepto.getText(), txtDocente.getText(), estado);
    }

    @FXML
    private void onCrear() {
        abrirFormulario(null);
    }

    @FXML
    private void onEliminarSeleccionados() {
        List<Integer> ids = data.stream()
            .filter(Deuda::isSeleccionado)
            .map(Deuda::getIdDeuda)
            .collect(Collectors.toList());

        if (ids.isEmpty()) {
            AlertUtil.advertencia("Sin selección", "Marque al menos una deuda para eliminar.");
            return;
        }

        if (!AlertUtil.confirmar("Eliminar deudas", "¿Está seguro de eliminar " + ids.size() + " deuda(s)?")) return;

        try {
            service.eliminarSeleccionados(ids);
            onBuscar();
            AlertUtil.info("Éxito", "Deudas eliminadas correctamente.");
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar:", e);
        }
    }

    private void confirmarEliminar(Deuda deuda) {
        if (!AlertUtil.confirmar("Eliminar deuda", "¿Eliminar deuda de " + deuda.getDocenteNombre() + "?")) return;
        try {
            service.eliminar(deuda.getIdDeuda());
            onBuscar();
        } catch (Exception e) {
            AlertUtil.error("Error", "No se pudo eliminar:", e);
        }
    }

    private void abrirFormulario(Deuda deuda) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/final2/deuda-form.fxml"));
            loader.setControllerFactory(AppContext.getInstance()::getController);
            Parent root = loader.load();

            DeudaFormController formCtrl = loader.getController();
            formCtrl.setDeuda(deuda);
            formCtrl.setOnGuardar(this::onBuscar);

            Stage modal = new Stage();
            modal.setTitle(deuda == null ? "Nueva Deuda" : "Editar Deuda");
            modal.setScene(new Scene(root));
            modal.initModality(Modality.APPLICATION_MODAL);
            modal.showAndWait();
        } catch (IOException e) {
            AlertUtil.error("Error", "Error al abrir el formulario:", e);
        }
    }
}
