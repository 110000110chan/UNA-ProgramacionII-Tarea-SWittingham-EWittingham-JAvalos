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
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author michw
 */
public class AdministradorMainController implements Initializable {

    @FXML
    private ToggleButton btnDashboard;
    @FXML
    private ToggleButton btnTramites;
    @FXML
    private ToggleButton btnClientes;
    @FXML
    private ToggleButton btnSucursales;
    @FXML
    private Button btnSalir;
    @FXML
    private StackPane contentArea;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
