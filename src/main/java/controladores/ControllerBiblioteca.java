package controladores;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

import BBDD.ConexionBBDD;
import dao.DaoAlumno;
import dao.DaoPrestamo;
import dao.DaoLibro;
import dao.DaoHistorialPrestamo;
import modelos.*;


import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ControllerBiblioteca {

    /**
     * Columna de la tabla que muestra el primer apellido del alumno.
     */
    @FXML
    private TableColumn<ModeloAlumno, String> colAlumnoApellido1;

    /**
     * Columna de la tabla que muestra el segundo apellido del alumno.
     */
    @FXML
    private TableColumn<ModeloAlumno, String> colAlumnoApellido2;

    /**
     * Columna de la tabla que muestra el DNI del alumno.
     */
    @FXML
    private TableColumn<ModeloAlumno, Integer> colAlumnoDni;

    /**
     * Columna de la tabla que muestra el nombre del alumno.
     */
    @FXML
    private TableColumn<ModeloAlumno, String> colAlumnoNombre;

    /**
     * Columna de la tabla que muestra el nombre del alumno en el histórico.
     */
    @FXML
    private TableColumn<ModeloHistorialPrestamo, String> colHistoricoAlumno;

    /**
     * Columna de la tabla que muestra la fecha de devolución del préstamo en el histórico.
     */
    @FXML
    private TableColumn<ModeloHistorialPrestamo, LocalDate> colHistoricoDevolucion;

    /**
     * Columna de la tabla que muestra la fecha de inicio del préstamo en el histórico.
     */
    @FXML
    private TableColumn<ModeloHistorialPrestamo, LocalDate> colHistoricoFecha;

    /**
     * Columna de la tabla que muestra el ID del histórico.
     */
    @FXML
    private TableColumn<ModeloHistorialPrestamo, Integer> colHistoricoID;

    /**
     * Columna de la tabla que muestra el Titulo del libro en el histórico.
     */
    @FXML
    private TableColumn<ModeloHistorialPrestamo, String> colHistoricoLibro;

    /**
     * Columna de la tabla que muestra el autor del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, String> colLibroAutor;

    /**
     * Columna de la tabla que muestra el código del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, Integer> colLibroCodigo;

    /**
     * Columna de la tabla que muestra la editorial del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, String> colLibroEditorial;

    /**
     * Columna de la tabla que muestra el estado del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, String> colLibroEstado;

    /**
     * Columna de la tabla que muestra la imagen del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, Blob> colLibroImagen;

    /**
     * Columna de la tabla que muestra el título del libro.
     */
    @FXML
    private TableColumn<ModeloLibro, String> colLibroTitulo;

    /**
     * Columna de la tabla que muestra el nombre del alumno en los préstamos.
     */
    @FXML
    private TableColumn<ModeloPrestamo, String> colPrestamoAlumno;

    /**
     * Columna de la tabla que muestra la fecha del préstamo.
     */
    @FXML
    private TableColumn<ModeloPrestamo, LocalDate> colPrestamoFecha;

    /**
     * Columna de la tabla que muestra el ID del préstamo.
     */
    @FXML
    private TableColumn<ModeloPrestamo, Integer> colPrestamoID;

    /**
     * Columna de la tabla que muestra el Titulo del libro en los préstamos.
     */
    @FXML
    private TableColumn<ModeloPrestamo, String> colPrestamoLibro;

    /**
     * Tabla de alumnos que se muestra en la interfaz.
     */
    @FXML
    private TableView<ModeloAlumno> tablaAlumno;

    /**
     * Tabla de históricos de préstamos que se muestra en la interfaz.
     */
    @FXML
    private TableView<ModeloHistorialPrestamo> tablaHistorico;

    /**
     * Tabla de libros que se muestra en la interfaz.
     */
    @FXML
    private TableView<ModeloLibro> tablaLibro;

    /**
     * Tabla de préstamos que se muestra en la interfaz.
     */
    @FXML
    private TableView<ModeloPrestamo> tablaPrestamo;

    /**
     * Botón que permite eliminar un libro.
     */
    @FXML
    private Button btnBajaLibro;

    /**
     * Botón que permite editar un alumno.
     */
    @FXML
    private Button btnEditarAlumno;

    /**
     * Botón que permite editar un libro.
     */
    @FXML
    private Button btnEditarLibro;

    /**
     * ComboBox para filtrar los históricos.
     */
    @FXML
    private ComboBox<String> comboFiltroHistorico;

    /**
     * Campo de texto para introducir un filtro de búsqueda en los históricos.
     */
    @FXML
    private TextField txtFiltroHistorico;

    /**
     * Botón que permite devolver un libro.
     */
    @FXML
    private Button btnDevolver;

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para añadir un nuevo libro.
     * Carga la ventana de añadir libro y permite interactuar con ella.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionAniadirLibro(ActionEvent event) {
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Libro.fxml"), bundle);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Añadir Libro");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarLibros());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para modificar un libro.
     * Carga la ventana de modificación con los datos del libro seleccionado.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionModificarLibro(ActionEvent event) {
        ModeloLibro libro=tablaLibro.getSelectionModel().getSelectedItem();
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Libro.fxml"),bundle);
            Parent root = fxmlLoader.load();

            ControllerLibro controller=fxmlLoader.getController();
            controller.setLibro(libro);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Editar Libro");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarLibros());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para dar de baja un libro.
     * Elimina el libro seleccionado de la base de datos.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionBajaLibro(ActionEvent event) {
        ModeloLibro libro=tablaLibro.getSelectionModel().getSelectedItem();
        if (DaoLibro.darDeBajaLibro(libro.getCodigo())){
            mostrarInfo("Libro dado de baja correctamente");
            cargarLibros();
        }else {
            mostrarInfo("Error al dar de baja el libro");
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para añadir un nuevo alumno.
     * Carga la ventana de añadir alumno y permite interactuar con ella.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionAniadirAlumno(ActionEvent event) {
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Alumno.fxml"),bundle);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Añadir Alumno");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarAlumnos());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para modificar un alumno.
     * Carga la ventana de modificación con los datos del alumno seleccionado.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionModificarAlumno(ActionEvent event) {
        ModeloAlumno alumno=tablaAlumno.getSelectionModel().getSelectedItem();
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Alumno.fxml"),bundle);
            Parent root = fxmlLoader.load();

            ControllerAlumno controller=fxmlLoader.getController();
            controller.setAlumno(alumno);

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Editar Alumno");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarAlumnos());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para crear un nuevo préstamo.
     * Carga la ventana de creación de préstamo y permite interactuar con ella.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionNuevoPrestamo(ActionEvent event) {
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Prestamo.fxml"),bundle);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Crear Prestamo");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarPrestamos());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para devolver un libro.
     * Carga la ventana de devolución de libro y permite interactuar con ella.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionDevolver(ActionEvent event) {
        try {
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");

            // Cargar el recurso de idioma adecuado
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Devolver.fxml"),bundle);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setTitle("Devolver Libro");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);

            // Establecer un evento que se ejecute cuando se cierre la ventana
            stage.setOnHidden(windowEvent -> cargarTodasTablas());
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para filtrar los historicos.
     * Filtra los datos de la tabla de historicos según el texto ingresado y el filtro seleccionado.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionFiltrarHistorico(ActionEvent event) {
        // Obtener el texto ingresado y el filtro seleccionado
        String filtro = comboFiltroHistorico.getValue();
        String textoFiltro = txtFiltroHistorico.getText().toLowerCase().trim();

        // Si el campo de texto está vacío, cargar todos los datos
        if (textoFiltro.isEmpty()) {
            cargarHistorico();
            return;
        }

        ObservableList<ModeloHistorialPrestamo> todosHistoricos = DaoHistorialPrestamo.todosHistoricos();

        if (filtro.equals("ID")) {
            tablaHistorico.setItems(todosHistoricos.filtered(historico ->
                    String.valueOf(historico.getId()).contains(textoFiltro)));
        } else if (filtro.equals("DNI")) {
            tablaHistorico.setItems(todosHistoricos.filtered(historico ->
                    historico.getAlumno().getDni().toLowerCase().contains(textoFiltro)));
        } else if (filtro.equals("Libro")) {
            tablaHistorico.setItems(todosHistoricos.filtered(historico ->
                    historico.getLibro().getTitulo().toLowerCase().contains(textoFiltro)));
        }

    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para generar el informe de libros.
     * Carga el informe de libros en formato Jasper y lo genera con los parámetros necesarios.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionInformeLibros(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();

        // Añadir la ruta de las imágenes
        String imagePath = getClass().getResource("/imagenes/").toString(); // Ruta de la carpeta de imágenes
        parameters.put("IMAGE_PATH", imagePath);
        String resourcePath = getClass().getResource("/jasper/").toString();
        parameters.put("Resource_PATH", resourcePath);
        generarInforme("Informe2.jasper",parameters);
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para generar el informe de gráficos.
     * Carga el informe de gráficos en formato Jasper y lo genera con los parámetros necesarios.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionInformeGraficos(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();

        // Añadir la ruta de las imágenes
        String imagePath = getClass().getResource("/imagenes/").toString(); // Ruta de la carpeta de imágenes
        parameters.put("IMAGE_PATH", imagePath);
        String resourcePath = getClass().getResource("/jasper/").toString();
        parameters.put("Resource_PATH", resourcePath);
        generarInforme("Informe3.jasper",parameters);
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón para generar el informe de alumnos.
     * Carga el informe de alumnos en formato Jasper y lo genera con los parámetros necesarios.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionInformeAlumnos(ActionEvent event) {
        Map<String, Object> parameters = new HashMap<>();

        // Añadir la ruta de las imágenes
        String imagePath = getClass().getResource("/imagenes/").toString(); // Ruta de la carpeta de imágenes
        parameters.put("IMAGE_PATH", imagePath);
        generarInforme("Informe4.jasper",parameters);
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón "Acerca de".
     * Muestra información sobre la aplicación, como el nombre y el autor.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionAcercaDe(ActionEvent event) {
        String msg="";
        msg += "Gestión de Biblioteca \n";
        msg += "Autor: Mikel Ramos Quesada";
        mostrarInfo(msg);
    }

    /**
     * Acción que se ejecuta cuando se hace clic en el botón "Ayuda".
     * Carga y muestra la guía de ayuda en una nueva ventana utilizando un WebView.
     *
     * Si ocurre algún error al cargar el archivo HTML, se muestra un mensaje de error.
     *
     * @param event El evento de acción generado por el clic en el botón.
     */
    @FXML
    void accionAyuda(ActionEvent event) {
        try {
            WebView webView = new WebView();
            WebEngine webEngine = webView.getEngine();

            // Asegúrate de que la ruta al archivo HTML es correcta
            String filePath = getClass().getResource("/html/ayuda.html").toExternalForm();
            webEngine.load(filePath);

            // Crear una nueva ventana para mostrar la guía
            Stage guiaStage = new Stage();
            guiaStage.setTitle("Ayuda");
            guiaStage.setScene(new Scene(webView, 800, 600)); // Tamaño adecuado
            guiaStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarInfo("Error al abrir la guía.");
        }
    }

    /**
     * Cambia el idioma de la aplicación a español y actualiza la ventana.
     * Este metodo guarda la configuración del idioma en la base de datos y luego
     * actualiza la ventana para reflejar los cambios en el idioma.
     *
     * @param event El evento de acción que dispara este metodo. No se usa explícitamente
     *              pero es necesario para la firma del metodo.
     */
    @FXML
    void cambiarEspanol(ActionEvent event) {
        ConexionBBDD.guardarIdioma("es");
        Stage stage = (Stage) txtFiltroHistorico.getScene().getWindow();
        actualizarVentana(stage);
    }

    /**
     * Cambia el idioma de la aplicación a inglés y actualiza la ventana.
     * Este metodo guarda la configuración del idioma en la base de datos y luego
     * actualiza la ventana para reflejar los cambios en el idioma.
     *
     * @param event El evento de acción que dispara este metodo. No se usa explícitamente
     *              pero es necesario para la firma del metodo.
     */
    @FXML
    void cambiarIngles(ActionEvent event) {
        ConexionBBDD.guardarIdioma("en");
        Stage stage = (Stage) txtFiltroHistorico.getScene().getWindow();
        actualizarVentana(stage);
    }

    /**
     * Actualiza la ventana con el nuevo idioma establecido en el sistema.
     * Este metodo carga las propiedades de idioma desde la base de datos,
     * crea un nuevo {@link Locale} según el idioma seleccionado, y luego carga
     * el archivo FXML de la ventana principal con el nuevo {@link ResourceBundle}.
     * Finalmente, cambia la raíz de la escena de la ventana principal para reflejar
     * los cambios de idioma.
     *
     * @param stage El {@link Stage} de la ventana principal que se actualizará.
     *              No debe ser nulo, ya que se usará para cambiar la escena.
     */
    public void actualizarVentana(Stage stage) {
        try {
            // Cargar las propiedades de idioma y establecer el nuevo locale
            Properties properties = ConexionBBDD.cargarIdioma();
            String lang = properties.getProperty("language");
            Locale locale = new Locale(lang);
            ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

            // Cargar el archivo FXML de la ventana principal con el nuevo ResourceBundle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Biblioteca.fxml"), bundle);
            Parent root = fxmlLoader.load();

            // Verificar que el Stage no sea nulo antes de cambiar la raíz de la escena
            if (stage != null) {
                stage.getScene().setRoot(root);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un informe Jasper utilizando un archivo Jasper y parámetros proporcionados.
     * La conexión a la base de datos se realiza a través de la clase ConexionBBDD.
     *
     * Si el archivo Jasper o los parámetros son inválidos, se muestra un mensaje de error.
     *
     * @param archivoJasper El nombre del archivo Jasper que define el informe a generar.
     * @param parameters Un mapa de parámetros que se pasará al informe (puede ser nulo).
     */
    private void generarInforme(String archivoJasper, Map<String, Object> parameters) {
        ConexionBBDD db;
        try {
            // Crear una nueva conexión a la base de datos
            db = new ConexionBBDD();

            // Cargar el archivo Jasper del informe
            InputStream reportStream = db.getClass().getResourceAsStream("/jasper/" + archivoJasper);

            // Verificar si el archivo fue encontrado
            if (reportStream == null) {
                System.out.println("Archivo NO encontrado");
                return;
            }

            // Cargar el informe Jasper
            JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);

            // Verificar si el mapa de parámetros es nulo o vacío
            if (parameters == null) {
                parameters = new HashMap<>();
            }

            // Añadir la ruta de las imágenes si no está ya en los parámetros
            if (!parameters.containsKey("IMAGE_PATH")) {
                String imagePath = db.getClass().getResource("/imagenes/").toString(); // Ruta de la carpeta de imágenes
                parameters.put("IMAGE_PATH", imagePath);
            }

            // Llenar el informe con datos
            JasperPrint jprint = JasperFillManager.fillReport(report, parameters, db.getConnection());

            // Mostrar el informe
            JasperViewer viewer = new JasperViewer(jprint, false);
            viewer.setVisible(true);

        } catch (SQLException e) {
            mostrarError("No se ha podido establecer conexión con la Base de Datos");
            e.printStackTrace();
        } catch (JRException e) {
            mostrarError("Error al procesar el informe Jasper");
            e.printStackTrace();
        }
    }

    /**
     * Carga la lista de libros activos en la tabla de libros.
     */
    void cargarLibros() {
        ObservableList<ModeloLibro> listaLibros = DaoLibro.todosLibrosActivos();
        tablaLibro.setItems(listaLibros);
    }

    /**
     * Carga la lista de alumnos en la tabla de alumnos.
     */
    void cargarAlumnos() {
        try {
            // Crear una instancia de ConexionBBDD para obtener la conexión
            ConexionBBDD conexionBBDD = new ConexionBBDD();
            Connection conexion = conexionBBDD.getConnection();

            // Llamar al metodo estático de DaoAlumno pasando la conexión
            List<ModeloAlumno> listaAlumnos = DaoAlumno.obtenerTodosLosAlumnos(conexion);

            // Convertir la lista de alumnos a ObservableList
            ObservableList<ModeloAlumno> observableAlumnos = FXCollections.observableArrayList(listaAlumnos);

            // Establecer los elementos de la tabla
            tablaAlumno.setItems(observableAlumnos);

            // Si es necesario cerrar la conexión, lo haces al final
            conexionBBDD.CloseConexion();

        } catch (SQLException e) {
            System.err.println("Error al obtener los alumnos: " + e.getMessage());
        }
    }

    /**
     * Carga la lista de préstamos en la tabla de préstamos.
     * También habilita o deshabilita el botón de "Devolver" dependiendo si hay préstamos registrados.
     */
    void cargarPrestamos() {
        ObservableList<ModeloPrestamo> listaPrestamos = DaoPrestamo.todosPrestamos();
        tablaPrestamo.setItems(listaPrestamos);

        // Verificar si la tabla está vacía
        if (listaPrestamos.isEmpty()) {
            btnDevolver.setDisable(true); // Deshabilitar el botón si no hay datos
        } else {
            btnDevolver.setDisable(false); // Habilitar el botón si hay datos
        }
    }

    /**
     * Carga la lista de históricos en la tabla de históricos.
     */
    void cargarHistorico() {
        ObservableList<ModeloHistorialPrestamo> listaHistoricos = DaoHistorialPrestamo.todosHistoricos();
        tablaHistorico.setItems(listaHistoricos);
    }

    /**
     * Carga todos los datos en las tablas: libros, alumnos, préstamos e históricos.
     */
    void cargarTodasTablas() {
        cargarLibros();
        cargarAlumnos();
        cargarPrestamos();
        cargarHistorico();
    }

    /**
     * Muestra un mensaje de error en una ventana de alerta.
     *
     * @param error El mensaje de error a mostrar en el cuadro de diálogo.
     */
    void mostrarError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    /**
     * Metodo que muestra un mensaje de información en una ventana emergente.
     *
     * @param info El mensaje de información que se mostrará en la ventana emergente.
     */
    void mostrarInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(info);
        alert.showAndWait();
    }

    /**
     * Metodo que se ejecuta al iniciar la ventana, inicializa las conexiones y configura las tablas y carga los combobox.
     *
     * @throws SQLException Si ocurre un error al intentar conectar con la base de datos.
     */
    @FXML
    void initialize() {
        // Controlar acceso a la base de datos
        try {
            new ConexionBBDD();
        } catch (SQLException e) {
            mostrarError("Conexion Erronea a la Base de Datos");
            Platform.exit(); // Cierra la aplicación
            return;
        }

        // Configuración de columnas para Alumnos
        colAlumnoDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colAlumnoNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAlumnoApellido1.setCellValueFactory(new PropertyValueFactory<>("apellido1"));
        colAlumnoApellido2.setCellValueFactory(new PropertyValueFactory<>("apellido2"));

        // Configuración de columnas para Libros
        colLibroCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colLibroTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colLibroAutor.setCellValueFactory(new PropertyValueFactory<>("autor"));
        colLibroEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        colLibroEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colLibroImagen.setCellValueFactory(new PropertyValueFactory<>("foto"));

        // Configuración del cell factory para mostrar las imágenes en la columna 'colLibroImagen'
        colLibroImagen.setCellFactory(column -> new TableCell<ModeloLibro, Blob>() {
            private final ImageView imageView = new ImageView();
            private final Image imagenPorDefecto = new Image(getClass().getResourceAsStream("/imagenes/iconoLibro.png"));

            @Override
            protected void updateItem(Blob item, boolean empty) {
                super.updateItem(item, empty);

                // Asegúrate de limpiar el gráfico si la celda está vacía
                if (empty) {
                    setGraphic(null);
                } else {
                    if (item == null) {
                        // No hay imagen, usar imagen por defecto
                        imageView.setImage(imagenPorDefecto);
                    } else {
                        try {
                            // Convierte el Blob a un Image
                            Image image = new Image(item.getBinaryStream());
                            imageView.setImage(image);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            // En caso de error, establecer la imagen por defecto
                            imageView.setImage(imagenPorDefecto);
                        }
                    }

                    // Ajusta el tamaño de la imagen y mantiene su proporción
                    imageView.setFitWidth(50); // Establece el ancho de la imagen
                    imageView.setFitHeight(50); // Establece la altura de la imagen
                    imageView.setPreserveRatio(true); // Mantiene la proporción de la imagen

                    // Establecer la gráfica de la celda
                    setGraphic(imageView); // Muestra la imagen en la celda
                }
            }
        });

        // Configuración de columnas para Histórico
        colHistoricoID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colHistoricoAlumno.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAlumno().getDni()));
        colHistoricoLibro.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getLibro().getTitulo()));

        // Configurar la columna de fecha de préstamo
        colHistoricoFecha.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFecha_prestamo()));

        // Aplicar un formato de fecha
        colHistoricoFecha.setCellFactory(column -> new TableCell<ModeloHistorialPrestamo, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Formatear la fecha
                    String formattedDate = item.format(formatter);
                    setText(formattedDate);
                }
            }
        });

        // Configurar la columna de fecha de devolución
        colHistoricoDevolucion.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getFecha_devolucion()));

        // Aplicar un formato de fecha
        colHistoricoDevolucion.setCellFactory(column -> new TableCell<ModeloHistorialPrestamo, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Formatear la fecha
                    String formattedDate = item.format(formatter);
                    setText(formattedDate);
                }
            }
        });


        // Configuración de columnas para Préstamos
        colPrestamoID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPrestamoAlumno.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getAlumno().getDni()));
        colPrestamoLibro.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getLibro().getTitulo()));

        // Configurar la columna de fecha de préstamo
        colPrestamoFecha.setCellValueFactory(new PropertyValueFactory<>("fecha_prestamo"));

        // Aplicar un formato de fecha
        colPrestamoFecha.setCellFactory(column -> new TableCell<ModeloPrestamo, LocalDate>() {
            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Formatear la fecha
                    String formattedDate = item.format(formatter);
                    setText(formattedDate);
                }
            }
        });

        //Cargar datos a las tablas
        cargarTodasTablas();


        // Deshabilitar los botones al inicio
        btnEditarLibro.setDisable(true);
        btnBajaLibro.setDisable(true);
        btnEditarAlumno.setDisable(true);

        // Agregar un listener a la tabla para habilitar/deshabilitar los botones
        tablaLibro.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean itemSeleccionado = newSelection != null;
            btnEditarLibro.setDisable(!itemSeleccionado);
            btnBajaLibro.setDisable(!itemSeleccionado);
        });
        tablaAlumno.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean itemSeleccionado = newSelection != null;
            btnEditarAlumno.setDisable(!itemSeleccionado);
        });

        //Cargar opciones al combobox del filtro historico
        comboFiltroHistorico.getItems().addAll(
                "ID",
                "DNI",
                "Libro"
        );
        //Elegir el primer item
        comboFiltroHistorico.getSelectionModel().selectFirst();

    }
}
