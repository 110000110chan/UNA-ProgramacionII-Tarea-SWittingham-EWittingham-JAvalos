package cr.ac.una.tareaprograii2026;

import cr.ac.una.tareaprograii2026.model.QueueManager;
import cr.ac.una.tareaprograii2026.util.FlowController;
import io.github.palexdev.materialfx.css.themes.MFXThemeManager;
import io.github.palexdev.materialfx.css.themes.Themes;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FlowController.getInstance().InitializeFlow(stage, null);
        
        // 2. Inicializamos el motor de colas (QueueManager) 
        // Al llamar a getInstance() por primera vez, se crea la instancia única.
        QueueManager.getInstance();
        
        scene = new Scene(loadFXML("AdministratorMain"), 640, 480);
        MFXThemeManager.addOn(scene, Themes.DEFAULT, Themes.LEGACY);
        
        String css = getClass().getResource("view/Styles.css").toExternalForm();
        scene.getStylesheets().add(css);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}
