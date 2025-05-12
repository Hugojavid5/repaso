package dao;
import BBDD.ConexionBBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelos.ModeloLibro;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;

/**
 * DAO para manejar la lógica de acceso a datos de libros en la base de datos.
 */
public class DaoLibro {
    /**
     * Obtiene una lista de todos los libros almacenados en la base de datos.
     *
     * @return Lista observable de libros.
     */
    public static ObservableList<ModeloLibro> todosLibros() {
        ConexionBBDD connection;
        ObservableList<ModeloLibro> libros = FXCollections.observableArrayList();
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT codigo,titulo,autor,editorial,estado,baja,imagen FROM Libro";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int codigo = rs.getInt(1);
                String titulo = rs.getString(2);
                String autor = rs.getString(3);
                String editorial = rs.getString(4);
                String estado = rs.getString(5);
                int baja = rs.getInt(6);
                Blob foto = rs.getBlob(7);
                ModeloLibro l=new ModeloLibro(codigo,titulo,autor,editorial,estado,baja,foto);
                libros.add(l);
            }
            rs.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return libros;
    }

    /**
     * Busca un libro en la base de datos por su código.
     *
     * @param codigo_libro Código del libro a buscar.
     * @return Objeto Libro si se encuentra, null en caso contrario.
     */
    public static ModeloLibro LibroID(String codigo_libro) {
        ConexionBBDD connection;
        ModeloLibro libro = null;
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT codigo,titulo,autor,editorial,estado,baja,imagen FROM Libro WHERE codigo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, codigo_libro);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int codigo = rs.getInt(1);
                String titulo = rs.getString(2);
                String autor = rs.getString(3);
                String editorial = rs.getString(4);
                String estado = rs.getString(5);
                int baja = rs.getInt(6);
                Blob foto = rs.getBlob(7);
                libro = new ModeloLibro(codigo,titulo,autor,editorial,estado,baja,foto);
            }
            rs.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return libro;
    }

    /**
     * Inserta un nuevo libro en la base de datos.
     *
     * @param l Objeto Libro a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public static boolean crearLibro(ModeloLibro l) {
        ConexionBBDD connection;
        int resul = 0;
        try {
            connection = new ConexionBBDD();
            String consulta = "INSERT INTO Libro (titulo,autor,editorial,estado,baja,imagen) VALUES (?,?,?,?,?,?) ";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, l.getTitulo());
            pstmt.setString(2, l.getAutor());
            pstmt.setString(3, l.getEditorial());
            pstmt.setString(4, l.getEstado());
            pstmt.setInt(5, l.getBaja());
            pstmt.setBlob(6, l.getFoto());

            resul = pstmt.executeUpdate();
            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resul > 0;
    }

    /**
     * Edita la información de un libro en la base de datos.
     *
     * @param l Objeto Libro con los nuevos datos.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean editarLibro(ModeloLibro l) {
        ConexionBBDD connection;
        int resul = 0;
        try {
            connection = new ConexionBBDD();
            String consulta = "UPDATE Libro SET titulo = ?,autor = ?,editorial = ?,estado = ?,baja = ?,imagen = ? WHERE codigo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, l.getTitulo());
            pstmt.setString(2, l.getAutor());
            pstmt.setString(3, l.getEditorial());
            pstmt.setString(4, l.getEstado());
            pstmt.setInt(5, l.getBaja());
            pstmt.setBlob(6, l.getFoto());
            pstmt.setInt(7, l.getCodigo());

            resul = pstmt.executeUpdate();
            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resul > 0;
    }

    /**
     * Marca un libro como dado de baja en la base de datos.
     *
     * @param codigo Código del libro a dar de baja.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public static boolean darDeBajaLibro(int codigo) {
        ConexionBBDD connection;
        int resul = 0;
        try {
            connection = new ConexionBBDD();
            // Consulta para actualizar el estado del libro a 'baja = 1'
            String consulta = "UPDATE Libro SET baja = 1 WHERE codigo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, codigo);

            resul = pstmt.executeUpdate();
            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resul > 0;
    }

    /**
     * Obtiene una lista de todos los libros que están activos (no dados de baja).
     *
     * @return Lista observable de libros activos.
     */
    public static ObservableList<ModeloLibro> todosLibrosActivos() {
        ConexionBBDD connection;
        ObservableList<ModeloLibro> libros = FXCollections.observableArrayList();
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT codigo,titulo,autor,editorial,estado,baja,imagen FROM Libro WHERE baja=0";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int codigo = rs.getInt(1);
                String titulo = rs.getString(2);
                String autor = rs.getString(3);
                String editorial = rs.getString(4);
                String estado = rs.getString(5);
                int baja = rs.getInt(6);
                Blob foto = rs.getBlob(7);
                ModeloLibro l=new ModeloLibro(codigo,titulo,autor,editorial,estado,baja,foto);
                libros.add(l);
            }
            rs.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return libros;
    }

    /**
     * Obtiene una lista de libros disponibles para ser prestados.
     * Un libro es considerado disponible si no está dado de baja y no está en préstamo.
     *
     * @return Lista observable de libros disponibles para préstamo.
     */
    public static ObservableList<ModeloLibro> todosLibrosParaPrestar() {
        ConexionBBDD connection;
        ObservableList<ModeloLibro> libros = FXCollections.observableArrayList();
        try {
            connection = new ConexionBBDD();
            String consulta = """
            SELECT l.codigo, l.titulo, l.autor, l.editorial, l.estado, l.baja, l.imagen
            FROM Libro l
            LEFT JOIN Prestamo p ON l.codigo = p.codigo_libro
            WHERE l.baja = 0 AND p.id_prestamo IS NULL
        """;
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int codigo = rs.getInt(1);
                String titulo = rs.getString(2);
                String autor = rs.getString(3);
                String editorial = rs.getString(4);
                String estado = rs.getString(5);
                int baja = rs.getInt(6);
                Blob foto = rs.getBlob(7);
                ModeloLibro l = new ModeloLibro(codigo, titulo, autor, editorial, estado, baja, foto);
                libros.add(l);
            }
            rs.close();
            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return libros;
    }


    /**
     * Convierte un archivo en un objeto Blob que puede ser almacenado en la base de datos.
     *
     * @param file El archivo a convertir.
     * @return Un objeto Blob con los datos del archivo.
     * @throws SQLException Si ocurre un error al crear el Blob.
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static Blob convertirFicheroABlob(File file) throws SQLException, IOException {
        ConexionBBDD connection = new ConexionBBDD();

        try (Connection conn = connection.getConnection();
             FileInputStream inputStream = new FileInputStream(file)) {
            Blob blob = conn.createBlob();

            byte[] buffer = new byte[1024];
            int bytesRead;

            try (var outputStream = blob.setBinaryStream(1)) {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            return blob;
        }
    }
}