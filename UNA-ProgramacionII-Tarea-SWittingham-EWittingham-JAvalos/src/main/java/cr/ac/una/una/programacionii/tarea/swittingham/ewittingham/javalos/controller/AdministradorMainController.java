package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.StackPane;

public class AdministradorMainController extends Controller implements Initializable {

    @FXML
    private ToggleButton btnDashboard;
    @FXML
    private ToggleButton btnTramites;
    @FXML
    private ToggleButton btnSucursales;
    @FXML
    private Button btnSalir;
    @FXML
    private StackPane contentArea;
    @FXML
    private ToggleButton btnCustomers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    
    
    public void mostrarVista(String nombreFXML) {
        try {
            Parent nuevaVista = FXMLLoader.load(getClass().getResource(
                    "/cr/ac/una/una/programacionii/tarea/swittingham/ewittingham/javalos/view/" + nombreFXML + ".fxml"
            ));
            contentArea.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
        }
    }

    @FXML
    private void showProcedure(ActionEvent event) {
    }

    @FXML
    private void showBranchesAndStations(ActionEvent event) {
    }

    @FXML
    private void showClients(ActionEvent event) {
        mostrarVista("ClientesView");
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
