package org.hugo.dein.repaso;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import BBDD.ConexionBBDD;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Clase principal de la aplicación de biblioteca que extiende Application.
 * Carga la interfaz gráfica y la configuración del idioma.
 */
public class BibliotecaApplication extends Application {

    /**
     * Metodo de inicio de la aplicación JavaFX.
     * Carga la configuración del idioma, inicializa la ventana principal
     * y muestra la interfaz de usuario.
     *
     * @param stage Escenario principal de la aplicación.
     * @throws IOException Si ocurre un error al cargar los archivos FXML o de recursos.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Cargar las propiedades de la base de datos
        Properties properties = ConexionBBDD.cargarIdioma();
        String lang = properties.getProperty("language");

        // Cargar el recurso de idioma adecuado utilizando el archivo de propiedades
        Locale locale = new Locale(lang);
        ResourceBundle bundle = ResourceBundle.getBundle("idiomas/lang", locale);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Biblioteca.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("LECTORIKA");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("/imagenes/logo.png")));
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metodo principal que lanza la aplicación.
     *
     * @param args Argumentos de línea de comandos.
     */
    public static void main(String[] args) {
        launch();
    }
}