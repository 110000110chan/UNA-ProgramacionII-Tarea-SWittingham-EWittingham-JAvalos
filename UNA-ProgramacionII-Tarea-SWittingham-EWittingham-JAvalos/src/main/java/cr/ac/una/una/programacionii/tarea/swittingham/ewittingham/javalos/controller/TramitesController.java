package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class TramitesController {

    @FXML
    private TextField txtId;

    @FXML
    private ComboBox<?> cbSucursal;

    @FXML
    private ComboBox<?> cbCliente;

    @FXML
    private ComboBox<?> cbEstacion;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private CheckBox chkAsignado;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private ComboBox<?> cbBuscapor;

    @FXML
    private TextField txtBuscar;

    @FXML
    private Button btnBuscar;

    @FXML
    private TableView<?> tbvTramites;

    @FXML
    private TableColumn<?, ?> tblId;

    @FXML
    private TableColumn<?, ?> tblCliente;

    @FXML
    private TableColumn<?, ?> tblSucursal;

    @FXML
    private TableColumn<?, ?> tblEstacion;

    @FXML
    private TableColumn<?, ?> tblDescripcion;

    @FXML
    private TableColumn<?, ?> tblAsignado;

    @FXML
    private void onActionBtnGuardar(ActionEvent event) {

    }

    @FXML
    private void onActionBtnLimpiar(ActionEvent event) {

    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {

    }
}