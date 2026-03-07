package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos;

import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
