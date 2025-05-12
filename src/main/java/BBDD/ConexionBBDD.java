package BBDD;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase para gestionar la conexión con la base de datos MariaDB.
 * Se encarga de establecer y cerrar la conexión con la base de datos.
 */
public class ConexionBBDD {

    /** Conexión activa a la base de datos. */
    private final Connection connection;

    /**
     * Constructor que establece la conexión con la base de datos.
     * Configura las propiedades de usuario y contraseña, y realiza la conexión
     * a una base de datos MariaDB en la dirección especificada.
     *
     * @throws SQLException si ocurre un error al establecer la conexión
     */
    public ConexionBBDD() throws SQLException {
        Properties connConfig = cargarProperties();
        String url=connConfig.getProperty("dburl");
        connection = DriverManager.getConnection(url, connConfig);
        connection.setAutoCommit(true);
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        /*
         System.out.println();
         System.out.println("--- Datos de conexión ------------------------------------------");
         System.out.printf("Base de datos: %s%n", databaseMetaData.getDatabaseProductName());
         System.out.printf("  Versión: %s%n", databaseMetaData.getDatabaseProductVersion());
         System.out.printf("Driver: %s%n", databaseMetaData.getDriverName());
         System.out.printf("  Versión: %s%n", databaseMetaData.getDriverVersion());
         System.out.println("----------------------------------------------------------------");
         System.out.println();
         */
        connection.setAutoCommit(true);
    }

    /**
     * Devuelve la conexión actual a la base de datos.
     *
     * @return la conexión activa
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Cierra la conexión activa con la base de datos.
     *
     * @return la conexión cerrada
     * @throws SQLException si ocurre un error al cerrar la conexión
     */
    public Connection CloseConexion() throws SQLException {
        connection.close();
        return connection;
    }

    /**
     * Carga las propiedades del archivo configuration.properties.
     *
     * @return the properties
     */
    public static Properties cargarProperties() {
        try (FileInputStream fs = new FileInputStream("configuration.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static Properties cargarIdioma() {
        try (FileInputStream fs = new FileInputStream("idioma.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void guardarIdioma(String nuevoIdioma) {
        Properties properties = cargarIdioma();
        if (properties != null) {
            properties.setProperty("language", nuevoIdioma);

            try (OutputStream output = new FileOutputStream("idioma.properties")) {
                properties.store(output, "");
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Crear una instancia de ConexionBBDD
            ConexionBBDD conexionBBDD = new ConexionBBDD();

            // Obtener la conexión
            Connection connection = conexionBBDD.getConnection();

            // Verificar si la conexión es válida
            if (connection != null && !connection.isClosed()) {
                System.out.println("Conexión a la base de datos establecida con éxito.");
            } else {
                System.out.println("No se pudo establecer la conexión a la base de datos.");
            }
            // Cerrar la conexión
            conexionBBDD.CloseConexion();
            System.out.println("Conexión cerrada.");
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
