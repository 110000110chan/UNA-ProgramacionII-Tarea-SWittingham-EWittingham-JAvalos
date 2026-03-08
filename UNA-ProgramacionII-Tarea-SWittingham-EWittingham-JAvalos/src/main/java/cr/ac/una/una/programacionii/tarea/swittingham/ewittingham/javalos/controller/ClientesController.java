/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author michw
 */
public class ClientesController implements Initializable {

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnRefrescar;
    @FXML
    private TableView<?> tblClientes;
    @FXML
    private TableColumn<?, ?> colFoto;
    @FXML
    private TableColumn<?, ?> colCedula;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colEdad;
    @FXML
    private TableColumn<?, ?> colFechaNacimiento;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colEmail;
    @FXML
    private TableColumn<?, ?> colFechaRegistro;
    @FXML
    private TableColumn<?, ?> colAcciones;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private Label lblFiltroActivo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
