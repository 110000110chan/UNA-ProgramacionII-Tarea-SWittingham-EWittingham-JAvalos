package cr.ac.una.tareaprograii2026.controller;

import cr.ac.una.tareaprograii2026.util.AppContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;

public class AdministratorMainController extends Controller implements Initializable {

    @FXML
    private ToggleButton btnDashboard;
    @FXML
    private ToggleButton btnProcedures;
    @FXML
    private ToggleButton btnCustomers;
    @FXML
    private ToggleButton btnLocations;
    @FXML
    private ToggleGroup menuButtons;
    @FXML
    private StackPane contentArea;
    @FXML
    private StackPane dashboardContainer;
    @FXML
    private StackPane proceduresContainer;
    @FXML
    private StackPane customersContainer;
    @FXML
    private StackPane locationsContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("admin_main_controller", this);
        
        menuButtons.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {

            dashboardContainer.getStyleClass().remove("active");
            proceduresContainer.getStyleClass().remove("active");
            customersContainer.getStyleClass().remove("active");
            locationsContainer.getStyleClass().remove("active");

        if (newToggle != null) {
            StackPane activeContainer = null;

            if (newToggle == btnDashboard) {
                activeContainer = dashboardContainer;
            } else if (newToggle == btnProcedures) {
                activeContainer = proceduresContainer;
                showView("TramitesView");
            } else if (newToggle == btnCustomers) {
                activeContainer = customersContainer;
                showView("CustomersView");
            } else if (newToggle == btnLocations) {
                activeContainer = locationsContainer;
                showView("LocationsManagement");
            }

            if (activeContainer != null) {
                activeContainer.getStyleClass().add("active");
            }
        }
    });
    }    
    
    public void showView(String nombreFXML) {
        try {
            Parent nuevaVista = FXMLLoader.load(getClass().getResource(
                    "/cr/ac/una/tareaprograii2026/view/" + nombreFXML + ".fxml"
            ));
            contentArea.getChildren().setAll(nuevaVista);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void showDashboard(ActionEvent event) {
        btnDashboard.setSelected(true);
    }

    @FXML
    private void showProcedures(ActionEvent event) {
        btnProcedures.setSelected(true);
    }

    @FXML
    private void showCustomers(ActionEvent event) {
        btnCustomers.setSelected(true);
    }

    @FXML
    private void showLocations(ActionEvent event) {
        btnLocations.setSelected(true);
    }

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }   
}