package cr.ac.una.tareaprograii2026.controller;

import cr.ac.una.tareaprograii2026.util.AppContext;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdministratorMainController extends Controller implements Initializable {

    @FXML private VBox sidebarContainer;
    @FXML private ToggleButton btnDashboard;
    @FXML private ToggleButton btnProcedures;
    @FXML private ToggleButton btnCustomers;
    @FXML private ToggleButton btnLocations;
    @FXML private ToggleGroup menuButtons;
    @FXML private StackPane contentArea;
    @FXML private StackPane dashboardContainer;
    @FXML private StackPane proceduresContainer;
    @FXML private StackPane customersContainer;
    @FXML private StackPane locationsContainer;
    @FXML private Label labelDashboard;
    @FXML private Label labelProcedures;
    @FXML private Label labelCustomers;
    @FXML private Label labelLocations;

    private Timeline timelineExpand;
    private Timeline timelineShrink;

    private final double WIDTH_NORMAL = 30.0;
    private final double WIDTH_HOVER = 123.2;
    
    private final double PADDING_NORMAL = 0.0;
    private final double PADDING_HOVER  = 38.0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppContext.getInstance().set("admin_main_controller", this);
    
        Duration duration = Duration.millis(300);

        timelineExpand = new Timeline();
        timelineExpand.getKeyFrames().addAll(
            new KeyFrame(duration,
                new KeyValue(sidebarContainer.prefWidthProperty(), WIDTH_HOVER),
                new KeyValue(labelDashboard.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_HOVER), Interpolator.EASE_BOTH),
                new KeyValue(labelProcedures.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_HOVER), Interpolator.EASE_BOTH),
                new KeyValue(labelCustomers.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_HOVER), Interpolator.EASE_BOTH),
                new KeyValue(labelLocations.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_HOVER), Interpolator.EASE_BOTH)
            )
        );

        timelineShrink = new Timeline();
        timelineShrink.getKeyFrames().addAll(
            new KeyFrame(duration,
                new KeyValue(sidebarContainer.prefWidthProperty(), WIDTH_NORMAL),
                new KeyValue(labelDashboard.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelDashboard.paddingProperty(), new Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelProcedures.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelProcedures.paddingProperty(), new Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelCustomers.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelCustomers.paddingProperty(), new Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelLocations.paddingProperty(), new javafx.geometry.Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH),
                new KeyValue(labelLocations.paddingProperty(), new Insets(0, 0, 0, PADDING_NORMAL), Interpolator.EASE_BOTH)
            )
        );

        sidebarContainer.setOnMouseEntered(e -> {
            timelineShrink.stop();
            timelineExpand.playFromStart();
        });

        sidebarContainer.setOnMouseExited(e -> {
            timelineExpand.stop();
            timelineShrink.playFromStart();
        });
        
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