package cr.ac.una.tareaprograii2026.controller;

import cr.ac.una.tareaprograii2026.util.AppContext;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class AdministratorMainController extends Controller implements Initializable {

    @FXML
    private MFXButton btnDashboard;
    @FXML
    private MFXButton btnProcedures;
    @FXML
    private MFXButton btnCustomers;
    @FXML
    private MFXButton btnLocations;
    @FXML
    private MFXButton btnExit;
    @FXML
    private StackPane contentArea;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("admin_main_controller", this);
    }    
    
    public void mostrarVista(String nombreFXML) {
        try {
            Parent nuevaVista = FXMLLoader.load(getClass().getResource(
                    "/cr/ac/una/tareaprograii2026/view/" + nombreFXML + ".fxml"
            ));
            contentArea.getChildren().setAll(nuevaVista);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showDashboard(ActionEvent event) {
    }
    
    @FXML
    private void showProcedures(ActionEvent event) {
        mostrarVista("TramitesView");
    }

    @FXML
    private void showLocations(ActionEvent event) {
                mostrarVista("LocationsManagement");
    }

    @FXML
    private void showCustomers(ActionEvent event) {
        mostrarVista("CustomersView");
    }
    
    @FXML
    private void showExit(ActionEvent event) {
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}

