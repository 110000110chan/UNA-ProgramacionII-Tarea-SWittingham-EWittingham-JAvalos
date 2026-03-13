package cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.controller;

import com.github.sarxos.webcam.Webcam;
import cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model.Customer;
import cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.model.FileManager;
import cr.ac.una.una.programacionii.tarea.swittingham.ewittingham.javalos.util.AppContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import javax.imageio.ImageIO;

public class CustomersController extends Controller implements Initializable {

    private static final String CUSTOMERS_FILE = "customers";

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnBuscar;
    @FXML
    private Button btnNuevo;
    @FXML
    private Button btnRefrescar;
    @FXML
    private TabPane tabPaneClientes;
    @FXML
    private Tab tabBusquedaClientes;
    @FXML
    private Tab tabRegistroCliente;
    @FXML
    private TableView<Customer> tblClientes;
    @FXML
    private TableColumn<Customer, String> colFoto;
    @FXML
    private TableColumn<Customer, String> colCedula;
    @FXML
    private TableColumn<Customer, String> colNombre;
    @FXML
    private TableColumn<Customer, Integer> colEdad;
    @FXML
    private TableColumn<Customer, String> colFechaNacimiento;
    @FXML
    private TableColumn<Customer, String> colTelefono;
    @FXML
    private TableColumn<Customer, String> colEmail;
    @FXML
    private TableColumn<Customer, String> colFechaRegistro;
    @FXML
    private TableColumn<Customer, Void> colAcciones;
    @FXML
    private TextField txtRegCedula;
    @FXML
    private TextField txtRegNombre;
    @FXML
    private DatePicker dpRegFechaNacimiento;
    @FXML
    private TextField txtRegTelefono;
    @FXML
    private TextField txtRegEmail;
    @FXML
    private Button btnAgregarFoto;
    @FXML
    private Button btnGuardarCliente;
    @FXML
    private Button btnLimpiarFormulario;
    @FXML
    private VBox paneFormularioCliente;
    @FXML
    private VBox paneFotoCliente;
    @FXML
    private ImageView imgViewFoto;
    @FXML
    private Button btnSeleccionarFoto;
    @FXML
    private Button btnTomarFoto;
    @FXML
    private Button btnVolverFormulario;
    @FXML
    private Label lblRutaFoto;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private Label lblFiltroActivo;

    private final FileManager fileManager = new FileManager();
    private ObservableList<Customer> clientes;
    private FilteredList<Customer> clientesFiltrados;
    private String fotoSeleccionadaPath;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarClientes();
        configurarEventos();
        mostrarPanelFormulario();
        actualizarEtiquetas();
    }

    @Override
    public void initialize() {
        // Requerido por clase base.
    }

    private void configurarEventos() {
        btnBuscar.setOnAction(event -> aplicarFiltroBusqueda());
        btnRefrescar.setOnAction(event -> refrescarTabla());
        btnNuevo.setOnAction(event -> abrirTabNuevoCliente());
        btnAgregarFoto.setOnAction(event -> {
            if (!validarFormularioSinFoto()) {
                return;
            }
            mostrarPanelFoto();
        });
        btnVolverFormulario.setOnAction(event -> mostrarPanelFormulario());
        btnSeleccionarFoto.setOnAction(event -> seleccionarFotoDesdeArchivo());
        btnTomarFoto.setOnAction(event -> tomarFotoDesdeCamara());
        btnGuardarCliente.setOnAction(event -> guardarCliente());
        btnLimpiarFormulario.setOnAction(event -> limpiarFormularioCompleto());
    }

    private void configurarTabla() {
        colCedula.setCellValueFactory(data -> new SimpleStringProperty(valorSeguro(data.getValue().getId())));
        colNombre.setCellValueFactory(data -> new SimpleStringProperty(nombreCompleto(data.getValue())));
        colEdad.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getAge()));
        colFechaNacimiento.setCellValueFactory(data -> new SimpleStringProperty(formatearFecha(data.getValue().getBirthDate())));
        colTelefono.setCellValueFactory(data -> new SimpleStringProperty(valorSeguro(data.getValue().getPhoneNumber())));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(valorSeguro(data.getValue().getEmail())));
        colFechaRegistro.setCellValueFactory(data -> new SimpleStringProperty("-"));
        colFoto.setCellValueFactory(data -> new SimpleStringProperty(valorSeguro(data.getValue().getPhotoUrl())));
        colFoto.setCellFactory(col -> new TableCell<>() {
            private final ImageView imageView = new ImageView();

            {
                imageView.setFitHeight(40);
                imageView.setFitWidth(40);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String path, boolean empty) {
                super.updateItem(path, empty);
                if (empty || path == null || path.isBlank()) {
                    setGraphic(null);
                    return;
                }
                Image image = crearImagenDesdeRuta(path);
                if (image == null || image.isError()) {
                    setGraphic(null);
                    return;
                }
                imageView.setImage(image);
                setGraphic(imageView);
            }
        });

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEliminar.setOnAction(event -> {
                    Customer customer = getTableView().getItems().get(getIndex());
                    clientes.remove(customer);
                    persistirClientes();
                    actualizarEtiquetas();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btnEliminar);
            }
        });
    }

    private void cargarClientes() {
        List<Customer> listaCargada = obtenerClientesDesdeContexto();
        if (listaCargada == null) {
            listaCargada = fileManager.deserialization(CUSTOMERS_FILE, Customer.class);
        }
        if (listaCargada == null) {
            listaCargada = new ArrayList<>();
        }

        clientes = FXCollections.observableArrayList(listaCargada);
        clientesFiltrados = new FilteredList<>(clientes, c -> true);
        tblClientes.setItems(clientesFiltrados);
        AppContext.getInstance().set("customers_list", new ArrayList<>(clientes));
    }

    @SuppressWarnings("unchecked")
    private List<Customer> obtenerClientesDesdeContexto() {
        Object data = AppContext.getInstance().get("customers_list");
        if (data instanceof List<?>) {
            return (List<Customer>) data;
        }
        return null;
    }

    private void abrirTabNuevoCliente() {
        tabPaneClientes.getSelectionModel().select(tabRegistroCliente);
        mostrarPanelFormulario();
        limpiarCamposFormulario();
    }

    private void mostrarPanelFormulario() {
        paneFormularioCliente.setManaged(true);
        paneFormularioCliente.setVisible(true);
        paneFotoCliente.setManaged(false);
        paneFotoCliente.setVisible(false);
    }

    private void mostrarPanelFoto() {
        paneFormularioCliente.setManaged(false);
        paneFormularioCliente.setVisible(false);
        paneFotoCliente.setManaged(true);
        paneFotoCliente.setVisible(true);
    }

    private void aplicarFiltroBusqueda() {
        String filtro = txtBuscar.getText() == null ? "" : txtBuscar.getText().trim().toLowerCase();
        clientesFiltrados.setPredicate(cliente -> {
            if (filtro.isEmpty()) {
                return true;
            }
            return valorSeguro(cliente.getId()).toLowerCase().contains(filtro)
                    || nombreCompleto(cliente).toLowerCase().contains(filtro)
                    || valorSeguro(cliente.getPhoneNumber()).toLowerCase().contains(filtro);
        });

        lblFiltroActivo.setText(filtro.isEmpty() ? "" : "Filtro: " + filtro);
        actualizarEtiquetas();
    }

    private void refrescarTabla() {
        txtBuscar.clear();
        clientesFiltrados.setPredicate(cliente -> true);
        lblFiltroActivo.setText("");
        tblClientes.refresh();
        actualizarEtiquetas();
    }

    private boolean validarFormularioSinFoto() {
        if (txtRegCedula.getText().isBlank()
                || txtRegNombre.getText().isBlank()
                || txtRegTelefono.getText().isBlank()
                || txtRegEmail.getText().isBlank()
                || dpRegFechaNacimiento.getValue() == null) {
            mostrarError("Completa todos los campos antes de continuar a la foto.");
            return false;
        }
        return true;
    }

    private void guardarCliente() {
        if (!validarFormularioSinFoto()) {
            return;
        }

        if (fotoSeleccionadaPath == null || fotoSeleccionadaPath.isBlank()) {
            mostrarError("Debes agregar o tomar una foto del cliente antes de guardar.");
            return;
        }

        String cedula = txtRegCedula.getText().trim();
        if (existeCedula(cedula)) {
            mostrarError("Ya existe un cliente con esa cédula.");
            return;
        }

        String nombreCompleto = txtRegNombre.getText().trim();
        String[] partesNombre = dividirNombre(nombreCompleto);

        Customer customer = new Customer(
                cedula,
                partesNombre[0],
                partesNombre[1],
                partesNombre[2],
                txtRegTelefono.getText().trim(),
                txtRegEmail.getText().trim(),
                fotoSeleccionadaPath,
                dpRegFechaNacimiento.getValue()
        );

        clientes.add(customer);
        persistirClientes();
        actualizarEtiquetas();
        limpiarFormularioCompleto();

        tabPaneClientes.getSelectionModel().select(tabBusquedaClientes);
        refrescarTabla();
    }

    private boolean existeCedula(String cedula) {
        return clientes.stream().anyMatch(c -> valorSeguro(c.getId()).equalsIgnoreCase(cedula));
    }

    private void persistirClientes() {
        fileManager.serialization(new ArrayList<>(clientes), CUSTOMERS_FILE);
        AppContext.getInstance().set("customers_list", new ArrayList<>(clientes));
        tblClientes.refresh();
    }

    private void limpiarFormularioCompleto() {
        limpiarCamposFormulario();
        limpiarFoto();
        mostrarPanelFormulario();
    }

    private void limpiarCamposFormulario() {
        txtRegCedula.clear();
        txtRegNombre.clear();
        txtRegTelefono.clear();
        txtRegEmail.clear();
        dpRegFechaNacimiento.setValue(null);
    }

    private void limpiarFoto() {
        fotoSeleccionadaPath = null;
        imgViewFoto.setImage(null);
        lblRutaFoto.setText("Ninguna foto seleccionada");
    }

    private void actualizarEtiquetas() {
        lblTotalClientes.setText("Total clientes: " + clientesFiltrados.size());
    }

    private void seleccionarFotoDesdeArchivo() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar foto del cliente");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Imagenes", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp")
        );

        Window owner = imgViewFoto.getScene() != null ? imgViewFoto.getScene().getWindow() : null;
        File archivoSeleccionado = fileChooser.showOpenDialog(owner);
        if (archivoSeleccionado == null) {
            return;
        }

        try {
            cargarFotoEnVista(archivoSeleccionado.toURI().toURL().toExternalForm(), archivoSeleccionado.getAbsolutePath());
        } catch (MalformedURLException ex) {
            mostrarError("No fue posible cargar la imagen seleccionada.");
        }
    }

    private void tomarFotoDesdeCamara() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            mostrarError("No se detecto una camara disponible en el equipo.");
            return;
        }

        try {
            webcam.open();
            BufferedImage bufferedImage = webcam.getImage();
            if (bufferedImage == null) {
                mostrarError("No fue posible capturar la foto desde la camara.");
                return;
            }

            Path carpetaFotos = Paths.get(System.getProperty("user.home"), "customer-photos");
            Files.createDirectories(carpetaFotos);
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            Path rutaFoto = carpetaFotos.resolve("cliente_" + timestamp + ".png");
            ImageIO.write(bufferedImage, "PNG", rutaFoto.toFile());

            cargarFotoEnVista(rutaFoto.toUri().toString(), rutaFoto.toAbsolutePath().toString());
        } catch (IOException ex) {
            mostrarError("Ocurrio un error guardando la foto capturada.");
        } finally {
            if (webcam.isOpen()) {
                webcam.close();
            }
        }
    }

    private void cargarFotoEnVista(String imageSource, String rutaTexto) {
        Image image = new Image(imageSource, true);
        if (image.isError()) {
            mostrarError("No fue posible abrir la imagen.");
            return;
        }

        imgViewFoto.setImage(image);
        fotoSeleccionadaPath = rutaTexto;
        lblRutaFoto.setText(rutaTexto);
    }

    private Image crearImagenDesdeRuta(String path) {
        try {
            if (path.startsWith("http") || path.startsWith("file:")) {
                return new Image(path, true);
            }
            return new Image(Paths.get(path).toUri().toString(), true);
        } catch (Exception ex) {
            return null;
        }
    }

    private String valorSeguro(String value) {
        return value == null ? "" : value;
    }

    private String nombreCompleto(Customer customer) {
        String nombre = valorSeguro(customer.getName());
        String apellido1 = valorSeguro(customer.getPaternalSurname());
        String apellido2 = valorSeguro(customer.getMaternalSurname());
        return (nombre + " " + apellido1 + " " + apellido2).trim().replaceAll("\\s+", " ");
    }

    private String formatearFecha(LocalDate fecha) {
        return fecha == null ? "" : fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    private String[] dividirNombre(String nombreCompleto) {
        String[] partes = nombreCompleto.trim().split("\\s+");
        String nombre = partes.length > 0 ? partes[0] : "";
        String apellido1 = partes.length > 1 ? partes[1] : "";
        String apellido2 = partes.length > 2 ? String.join(" ", java.util.Arrays.copyOfRange(partes, 2, partes.length)) : "";
        return new String[]{nombre, apellido1, apellido2};
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Operacion no completada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
