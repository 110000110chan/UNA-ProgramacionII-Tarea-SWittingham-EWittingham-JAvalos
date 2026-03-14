package cr.ac.una.tareaprograii2026.controller;

import cr.ac.una.tareaprograii2026.model.Customer;
import cr.ac.una.tareaprograii2026.model.FileManager;
import cr.ac.una.tareaprograii2026.util.AppContext;
import cr.ac.una.tareaprograii2026.util.PhotoStorageUtil;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class CustomersController extends Controller implements Initializable {

    private static final String CUSTOMERS_FILE = "customers";
    private static final String CUSTOMER_DRAFT_KEY = "customer_form_draft";
    private static final String PHOTO_RESULT_PATH_KEY = "photo_result_path";
    private static final String PHOTO_REQUEST_OWNER_KEY = "photo_request_owner";
    private static final String PHOTO_RETURN_VIEW_KEY = "photo_return_view";

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
    private ImageView imgPreviewFotoCliente;
    @FXML
    private Label lblFotoCliente;
    @FXML
    private Button btnAgregarFoto;
    @FXML
    private Button btnGuardarCliente;
    @FXML
    private Button btnLimpiarFormulario;
    @FXML
    private Label lblTotalClientes;
    @FXML
    private Label lblFiltroActivo;

    private final FileManager fileManager = new FileManager();
    private ObservableList<Customer> clientes;
    private FilteredList<Customer> clientesFiltrados;
    private String fotoSeleccionadaPath;

    @Override
    public void initialize(java.net.URL url, ResourceBundle rb) {
        configurarTabla();
        cargarClientes();
        configurarEventos();
        restaurarBorradorSiExiste();
        actualizarEtiquetas();
    }

    @Override
    public void initialize() {
    }

    private void configurarEventos() {
        btnBuscar.setOnAction(event -> aplicarFiltroBusqueda());
        btnRefrescar.setOnAction(event -> refrescarTabla());
        btnNuevo.setOnAction(event -> abrirTabNuevoCliente());
        btnAgregarFoto.setOnAction(event -> abrirVistaFotografia());
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
                Image image = PhotoStorageUtil.createImageFromPath(path);
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

    @SuppressWarnings("unchecked")
    private void restaurarBorradorSiExiste() {
        Object draftData = AppContext.getInstance().get(CUSTOMER_DRAFT_KEY);
        if (!(draftData instanceof Map<?, ?> draftMap)) {
            limpiarPreviewFoto();
            tabPaneClientes.getSelectionModel().select(tabBusquedaClientes);
            return;
        }

        txtRegCedula.setText(valorSeguro((String) draftMap.get("cedula")));
        txtRegNombre.setText(valorSeguro((String) draftMap.get("nombre")));
        txtRegTelefono.setText(valorSeguro((String) draftMap.get("telefono")));
        txtRegEmail.setText(valorSeguro((String) draftMap.get("email")));

        Object fecha = draftMap.get("fechaNacimiento");
        if (fecha instanceof LocalDate localDate) {
            dpRegFechaNacimiento.setValue(localDate);
        }

        String photoPath = valorSeguro((String) AppContext.getInstance().get(PHOTO_RESULT_PATH_KEY));
        if (photoPath.isBlank()) {
            photoPath = valorSeguro((String) draftMap.get("foto"));
        }

        if (!photoPath.isBlank()) {
            setFotoSeleccionada(photoPath);
        } else {
            limpiarPreviewFoto();
        }

        tabPaneClientes.getSelectionModel().select(tabRegistroCliente);
    }

    private void abrirTabNuevoCliente() {
        limpiarBorradorEnContexto();
        limpiarFormularioCompleto();
        tabPaneClientes.getSelectionModel().select(tabRegistroCliente);
    }

    private void abrirVistaFotografia() {
        if (!validarFormularioSinFoto()) {
            return;
        }

        guardarBorradorCliente();
        AppContext.getInstance().set(PHOTO_REQUEST_OWNER_KEY, "customer");
        AppContext.getInstance().set(PHOTO_RETURN_VIEW_KEY, "CustomersView");

        AdministratorMainController adminController = (AdministratorMainController) AppContext.getInstance().get("admin_main_controller");
        if (adminController != null) {
            adminController.mostrarVista("Photography");
        }
    }

    private void guardarBorradorCliente() {
        Map<String, Object> draft = new HashMap<>();
        draft.put("cedula", txtRegCedula.getText().trim());
        draft.put("nombre", txtRegNombre.getText().trim());
        draft.put("telefono", txtRegTelefono.getText().trim());
        draft.put("email", txtRegEmail.getText().trim());
        draft.put("fechaNacimiento", dpRegFechaNacimiento.getValue());
        draft.put("foto", fotoSeleccionadaPath);
        AppContext.getInstance().set(CUSTOMER_DRAFT_KEY, draft);
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
        txtRegCedula.clear();
        txtRegNombre.clear();
        txtRegTelefono.clear();
        txtRegEmail.clear();
        dpRegFechaNacimiento.setValue(null);
        limpiarPreviewFoto();
        limpiarBorradorEnContexto();
    }

    private void limpiarBorradorEnContexto() {
        AppContext.getInstance().delete(CUSTOMER_DRAFT_KEY);
        AppContext.getInstance().delete(PHOTO_RESULT_PATH_KEY);
        AppContext.getInstance().delete(PHOTO_REQUEST_OWNER_KEY);
        AppContext.getInstance().delete(PHOTO_RETURN_VIEW_KEY);
    }

    private void setFotoSeleccionada(String rutaFoto) {
        Image image = PhotoStorageUtil.createImageFromPath(rutaFoto);
        if (image == null || image.isError()) {
            limpiarPreviewFoto();
            mostrarError("No fue posible cargar la foto seleccionada.");
            return;
        }

        fotoSeleccionadaPath = rutaFoto;
        imgPreviewFotoCliente.setImage(image);
        lblFotoCliente.setText(rutaFoto);
        guardarBorradorCliente();
    }

    private void limpiarPreviewFoto() {
        fotoSeleccionadaPath = null;
        imgPreviewFotoCliente.setImage(null);
        lblFotoCliente.setText("Ninguna foto seleccionada");
    }

    private void actualizarEtiquetas() {
        lblTotalClientes.setText("Total clientes: " + clientesFiltrados.size());
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
        alert.setHeaderText("Operación no completada");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}