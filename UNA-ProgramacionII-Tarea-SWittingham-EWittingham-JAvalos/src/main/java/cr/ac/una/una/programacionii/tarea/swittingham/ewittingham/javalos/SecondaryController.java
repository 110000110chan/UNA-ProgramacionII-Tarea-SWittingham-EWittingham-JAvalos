package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos;

import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}