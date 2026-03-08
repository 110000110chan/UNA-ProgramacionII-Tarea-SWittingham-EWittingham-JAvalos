package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
}
