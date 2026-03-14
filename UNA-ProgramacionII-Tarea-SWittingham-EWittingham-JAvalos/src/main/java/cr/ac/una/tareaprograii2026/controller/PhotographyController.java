package cr.ac.una.tareaprograii2026.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.tareaprograii2026.util.AppContext;
import cr.ac.una.tareaprograii2026.util.PhotoStorageUtil;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import java.awt.Color;

public class PhotographyController implements Initializable {

    private static final String PHOTO_RESULT_PATH_KEY = "photo_result_path";
    private static final String PHOTO_REQUEST_OWNER_KEY = "photo_request_owner";
    private static final String PHOTO_RETURN_VIEW_KEY = "photo_return_view";

    @FXML
    private Label lblTituloFoto;
    @FXML
    private ImageView imgViewFoto;
    @FXML
    private Button btnStartCamera;
    @FXML
    private Button btnCapturePhoto;
    @FXML
    private Button btnStopCamera;
    @FXML
    private ImageView imgViewFoto1;
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private Button btnNaturalFilter;
    @FXML
    private Button btnVintageFilter;
    @FXML
    private Button btnCoolFilter;
    @FXML
    private Button btnWarmFilter;
    @FXML
    private Button btnVolverFormulario;
    @FXML
    private Label lblRutaFoto;

    private Webcam webcam;
    private Timeline previewTimeline;
    private BufferedImage currentFrame;
    private String selectedPhotoPath;
    private ColorAdjust currentFilter;
    private boolean camaraRealDisponible;  // Track if physical camera exists
    private BufferedImage currentPhotoWithoutFilter;  // Store photo before any filter applied
    private BufferedImage currentPhotoWithFilter;  // Store the in-memory processed photo

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarContexto();
        configurarEventos();
        verificarDisponibilidadCamara();
        restaurarFotoExistente();
    }

    private void configurarContexto() {
        String owner = (String) AppContext.getInstance().get(PHOTO_REQUEST_OWNER_KEY);
        if ("customer".equalsIgnoreCase(owner)) {
            lblTituloFoto.setText("Fotografía del Cliente");
        } else if (owner != null && !owner.isBlank()) {
            lblTituloFoto.setText("Fotografía de " + owner);
        }
    }

    private void configurarEventos() {
        btnStartCamera.setOnAction(event -> iniciarCamara());
        btnCapturePhoto.setOnAction(event -> capturarFoto());
        btnStopCamera.setOnAction(event -> detenerCamara());
        btnSeleccionarFoto.setOnAction(event -> seleccionarFotoDesdeArchivo());
        btnNaturalFilter.setOnAction(event -> aplicarFiltro(null));
        btnVintageFilter.setOnAction(event -> aplicarFiltro(crearFiltroVintage()));
        btnCoolFilter.setOnAction(event -> aplicarFiltro(crearFiltroFrio()));
        btnWarmFilter.setOnAction(event -> aplicarFiltro(crearFiltroCalido()));
        btnVolverFormulario.setOnAction(event -> volverAVistaAnterior());
    }

    private void restaurarFotoExistente() {
        String existingPath = (String) AppContext.getInstance().get(PHOTO_RESULT_PATH_KEY);
        if (existingPath == null || existingPath.isBlank()) {
            return;
        }

        Image image = PhotoStorageUtil.createImageFromPath(existingPath);
        if (image == null || image.isError()) {
            return;
        }

        selectedPhotoPath = existingPath;
        imgViewFoto1.setImage(image);
        lblRutaFoto.setText(existingPath);
    }

    private void verificarDisponibilidadCamara() {
        List<Webcam> webcams = Webcam.getWebcams();
        camaraRealDisponible = false;

        if (webcams != null && !webcams.isEmpty()) {
            // Check if any real camera exists (not virtual)
            for (Webcam cam : webcams) {
                if (prioridadCamara(cam) == 0) {  // Priority 0 = real camera
                    camaraRealDisponible = true;
                    System.out.println("DEBUG: Real camera found: " + cam.getName());
                    break;
                }
            }
        }

        // If no real camera, disable camera controls and inform user
        if (!camaraRealDisponible) {
            System.out.println("DEBUG: No real cameras found - disabling camera controls");
            btnStartCamera.setDisable(true);
            btnCapturePhoto.setDisable(true);
            btnStopCamera.setDisable(true);
            
            StringBuilder mensaje = new StringBuilder();
            mensaje.append("⚠ No se detectó cámara física disponible\n\n");
            mensaje.append("Cámaras encontradas:\n");
            if (webcams != null) {
                for (Webcam cam : webcams) {
                    mensaje.append("• ").append(cam.getName()).append(" (Virtual)\n");
                }
            } else {
                mensaje.append("• Ninguna\n");
            }
            mensaje.append("\nSolución: Usa 'Seleccionar Foto' para\n");
            mensaje.append("cargar una imagen desde tus archivos.");
            
            lblRutaFoto.setText(mensaje.toString());
            lblRutaFoto.setStyle("-fx-text-fill: #ff6600; -fx-font-size: 11;");
        } else {
            lblRutaFoto.setStyle("-fx-text-fill: black;");
            lblRutaFoto.setText("Ingrese una fotografía");
        }
    }

    private void iniciarCamara() {
        if (webcam != null && webcam.isOpen()) {
            return;
        }

        if (!camaraRealDisponible) {
            mostrarError("No hay cámara física disponible en este equipo.\n\n" +
                    "Por favor, usa la opción 'Seleccionar Foto' para\n" +
                    "cargar una imagen desde tus archivos.");
            return;
        }

        System.out.println("DEBUG: iniciarCamara() called - starting camera selection");
        lblRutaFoto.setText("Abriendo cámara...");
        
        webcam = seleccionarCamaraPreferida();
        if (webcam == null) {
            System.out.println("DEBUG: Camera selection returned null");
            lblRutaFoto.setText("✗ Error: No se pudo acceder a la cámara");
            mostrarError("No fue posible acceder a la cámara disponible.");
            return;
        }

        System.out.println("DEBUG: Attempting to open: " + webcam.getName());
        
        try {
            configurarResolucion(webcam);
            webcam.open();
            System.out.println("DEBUG: Camera opened successfully: " + webcam.getName());
            previewTimeline = new Timeline(new KeyFrame(Duration.millis(150), event -> actualizarPreview()));
            previewTimeline.setCycleCount(Timeline.INDEFINITE);
            previewTimeline.play();
            actualizarPreview();
            lblRutaFoto.setText("✓ Cámara activa: " + webcam.getName());
        } catch (Exception ex) {
            System.out.println("DEBUG: Failed to open camera: " + ex.getMessage());
            ex.printStackTrace();
            lblRutaFoto.setText("✗ Fallo al abrir: " + (webcam != null ? webcam.getName() : "cámara"));
            mostrarError("No fue posible iniciar la cámara.\n\nInténtalo de nuevo o usa la opción\n'Seleccionar Foto' para cargar un archivo.");
            webcam = null;
        }
    }

    private void actualizarPreview() {
        if (webcam == null || !webcam.isOpen()) {
            return;
        }

        BufferedImage frame = webcam.getImage();
        if (frame == null) {
            return;
        }

        currentFrame = frame;
        Image image = SwingFXUtils.toFXImage(frame, null);
        imgViewFoto.setImage(image);
        imgViewFoto.setEffect(currentFilter);
    }

    private void capturarFoto() {
        if (currentFrame == null) {
            mostrarError("Primero inicia la cámara y asegúrate de que haya una imagen disponible.");
            return;
        }

        try {
            // Store the unfiltered image for later filter changes
            currentPhotoWithoutFilter = currentFrame;
            
            // Process the image with the current filter (in memory only)
            if (currentFilter != null) {
                currentPhotoWithFilter = aplicarFiltroABufferedImage(currentFrame, currentFilter);
            } else {
                currentPhotoWithFilter = currentFrame;
            }

            // Display the processed image from memory
            Image image = SwingFXUtils.toFXImage(currentPhotoWithFilter, null);
            imgViewFoto1.setImage(image);
            imgViewFoto1.setEffect(null);  
            lblRutaFoto.setText("Foto capturada (sin guardar)");
        } catch (Exception ex) {
            mostrarError("No fue posible procesar la foto capturada.");
        }
    }

    private void seleccionarFotoDesdeArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        File selectedFile = fileChooser.showOpenDialog(btnSeleccionarFoto.getScene().getWindow());
        if (selectedFile == null) {
            return;
        }

        try {
            // Copy the file to the project
            selectedPhotoPath = PhotoStorageUtil.copyPhotoToProject(selectedFile.toPath(), obtenerPrefijo());
            
            // Load the image and store it without filter
            currentPhotoWithoutFilter = javax.imageio.ImageIO.read(new File(selectedPhotoPath));
            
            // Display the image without any filter effects
            Image image = PhotoStorageUtil.createImageFromPath(selectedPhotoPath);
            imgViewFoto1.setImage(image);
            imgViewFoto1.setEffect(null);
            lblRutaFoto.setText(selectedPhotoPath);
            AppContext.getInstance().set(PHOTO_RESULT_PATH_KEY, selectedPhotoPath);
            
            // Reset currentFilter so user can choose a filter intentionally
            currentFilter = null;
        } catch (IOException ex) {
            mostrarError("No fue posible copiar la imagen seleccionada.");
        }
    }

    private void detenerCamara() {
        if (previewTimeline != null) {
            previewTimeline.stop();
            previewTimeline = null;
        }
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
        webcam = null;
    }

    private Webcam seleccionarCamaraPreferida() {
        List<Webcam> webcams = Webcam.getWebcams();
        if (webcams == null || webcams.isEmpty()) {
            System.out.println("DEBUG: No cameras detected at all");
            return null;
        }

        System.out.println("DEBUG: Total cameras detected: " + webcams.size());
        for (Webcam cam : webcams) {
            int priority = prioridadCamara(cam);
            String type = (priority == 0) ? "REAL" : "VIRTUAL";
            System.out.println("DEBUG: Camera - Name: " + cam.getName() + ", Priority: " + priority + ", Type: " + type);
        }

        // Try to find a real camera (priority 0)
        Webcam selected = webcams.stream()
                .filter(cam -> prioridadCamara(cam) == 0)
                .findFirst()
                .orElse(null);

        // If no real camera found, try any camera
        if (selected == null && !webcams.isEmpty()) {
            System.out.println("DEBUG: No real camera found, attempting with any available camera");
            selected = webcams.get(0);
        }

        if (selected != null) {
            System.out.println("DEBUG: Selected camera: " + selected.getName() + " (Priority: " + prioridadCamara(selected) + ")");
        } else {
            System.out.println("DEBUG: No camera selected");
        }

        return selected;
    }

    private int prioridadCamara(Webcam candidate) {
        String name = candidate.getName() == null ? "" : candidate.getName().toLowerCase();
        if (name.contains("vseeface") || name.contains("virtual") || name.contains("obs") || name.contains("snap")) {
            return 10;
        }
        return 0;
    }

    private void configurarResolucion(Webcam candidate) {
        Dimension[] sizes = candidate.getViewSizes();
        if (sizes == null || sizes.length == 0) {
            return;
        }

        Dimension selectedSize = java.util.Arrays.stream(sizes)
                .filter(size -> size.width <= 1280)
                .max(Comparator.comparingInt(size -> size.width * size.height))
                .orElse(sizes[0]);
        candidate.setViewSize(selectedSize);
    }

    private void aplicarFiltro(ColorAdjust filter) {
        currentFilter = filter;
        
        // Apply effect to live camera preview
        imgViewFoto.setEffect(filter);
        
        // If there's a captured/selected photo, re-process it with the new filter (in memory only)
        if (currentPhotoWithoutFilter != null) {
            try {
                // Process in memory WITHOUT saving to disk yet
                if (filter != null) {
                    currentPhotoWithFilter = aplicarFiltroABufferedImage(currentPhotoWithoutFilter, filter);
                } else {
                    currentPhotoWithFilter = currentPhotoWithoutFilter;
                }
                
                // Display the processed image from memory
                Image image = SwingFXUtils.toFXImage(currentPhotoWithFilter, null);
                imgViewFoto1.setImage(image);
                imgViewFoto1.setEffect(null);
                lblRutaFoto.setText("Foto en memoria (sin guardar)");
            } catch (Exception ex) {
                System.err.println("Error applying filter to photo: " + ex.getMessage());
            }
        }
    }

    /**
     * Applies a ColorAdjust filter to a BufferedImage by processing each pixel.
     * This creates a new processed image without modifying the original.
     */
    private BufferedImage aplicarFiltroABufferedImage(BufferedImage original, ColorAdjust filter) {
        if (filter == null) {
            return original;
        }

        BufferedImage resultado = new BufferedImage(
                original.getWidth(), 
                original.getHeight(), 
                BufferedImage.TYPE_INT_RGB
        );

        float hue = (float) filter.getHue();           // Range: -1.0 to 1.0
        float saturation = (float) filter.getSaturation(); // Range: -1.0 to 1.0
        float brightness = (float) filter.getBrightness();  // Range: -1.0 to 1.0
        float contrast = (float) filter.getContrast();    // Range: -1.0 to 1.0

        for (int y = 0; y < original.getHeight(); y++) {
            for (int x = 0; x < original.getWidth(); x++) {
                int rgb = original.getRGB(x, y);
                
                // Extract ARGB components
                int alpha = (rgb >> 24) & 0xFF;
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // Convert RGB to HSB
                float[] hsb = Color.RGBtoHSB(r, g, b, null);

                // Apply Hue adjustment (convert -1..1 to -360..360 degrees)
                hsb[0] += hue;
                if (hsb[0] > 1) hsb[0] -= 1;
                if (hsb[0] < 0) hsb[0] += 1;

                // Apply Saturation adjustment
                hsb[1] = Math.max(0, Math.min(1, hsb[1] + saturation));

                // Apply Brightness adjustment
                hsb[2] = Math.max(0, Math.min(1, hsb[2] + brightness));

                // Convert HSB back to RGB
                int newRGB = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);

                // Extract new RGB components
                int newR = (newRGB >> 16) & 0xFF;
                int newG = (newRGB >> 8) & 0xFF;
                int newB = newRGB & 0xFF;

                // Apply Contrast adjustment
                // Contrast formula: output = ((input - 128) * contrast) + 128
                if (contrast != 0) {
                    newR = (int) Math.max(0, Math.min(255, ((newR - 128) * (1 + contrast)) + 128));
                    newG = (int) Math.max(0, Math.min(255, ((newG - 128) * (1 + contrast)) + 128));
                    newB = (int) Math.max(0, Math.min(255, ((newB - 128) * (1 + contrast)) + 128));
                }

                // Reconstruct ARGB
                int resultRGB = (alpha << 24) | (newR << 16) | (newG << 8) | newB;
                resultado.setRGB(x, y, resultRGB);
            }
        }

        return resultado;
    }

    private ColorAdjust crearFiltroVintage() {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(-0.025);
        adjust.setSaturation(-0.15);
        adjust.setBrightness(0.04);
        adjust.setContrast(0.02);
        return adjust;
    }

    private ColorAdjust crearFiltroFrio() {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(0.04);
        adjust.setSaturation(0.06);
        adjust.setBrightness(-0.02);
        return adjust;
    }

    private ColorAdjust crearFiltroCalido() {
        ColorAdjust adjust = new ColorAdjust();
        adjust.setHue(-0.04);
        adjust.setSaturation(0.1);
        adjust.setBrightness(0.025);
        return adjust;
    }

    private String obtenerPrefijo() {
        String owner = (String) AppContext.getInstance().get(PHOTO_REQUEST_OWNER_KEY);
        if ("customer".equalsIgnoreCase(owner)) {
            return "cliente";
        }
        if (owner == null || owner.isBlank()) {
            return "foto";
        }
        return owner.toLowerCase();
    }

    private void volverAVistaAnterior() {
        detenerCamara();

        // If there's a photo with filter in memory, save it NOW before returning
        if (currentPhotoWithFilter != null) {
            try {
                selectedPhotoPath = PhotoStorageUtil.saveBufferedImage(currentPhotoWithFilter, obtenerPrefijo());
                currentPhotoWithFilter = null;  // Clear memory
            } catch (IOException ex) {
                System.err.println("Error saving filtered photo: " + ex.getMessage());
            }
        }

        if (selectedPhotoPath != null && !selectedPhotoPath.isBlank()) {
            AppContext.getInstance().set(PHOTO_RESULT_PATH_KEY, selectedPhotoPath);
        }

        String returnView = (String) AppContext.getInstance().get(PHOTO_RETURN_VIEW_KEY);
        if (returnView == null || returnView.isBlank()) {
            returnView = "CustomersView";
        }

        AdministratorMainController adminController = (AdministratorMainController) AppContext.getInstance().get("admin_main_controller");
        if (adminController != null) {
            adminController.mostrarVista(returnView);
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operación no completada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}