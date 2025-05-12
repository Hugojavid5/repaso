package dao;

import BBDD.ConexionBBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelos.ModeloAlumno;
import modelos.ModeloLibro;
import modelos.ModeloPrestamo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DaoPrestamo {

    public static ObservableList<ModeloPrestamo> todosPrestamos() {
        ConexionBBDD connection;
        ObservableList<ModeloPrestamo> prestamos = FXCollections.observableArrayList();
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo FROM Prestamo";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id_prestamo = rs.getInt(1);
                String dni_alumno = rs.getString(2);
                String codigo_libro = rs.getString(3);
                LocalDate fecha_prestamo = rs.getDate(4).toLocalDate();

                ModeloAlumno a= DaoAlumno.obtenerAlumnoPorDni(dni_alumno);
                ModeloLibro l= DaoLibro.LibroID(codigo_libro);

                ModeloPrestamo p =new ModeloPrestamo(id_prestamo,a,l,fecha_prestamo);
                prestamos.add(p);
            }
            rs.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return prestamos;
    }

    /**
     * Obtiene todos los préstamos registrados en la base de datos.
     *
     * @return Una lista observable de objetos {@link ModeloPrestamo} que contienen los detalles de los préstamos.
     */
    public static int crearPrestamo(ModeloPrestamo p) {
        ConexionBBDD connection;
        int generatedId = -1; // Valor por defecto en caso de error o si no se genera un ID
        try {
            connection = new ConexionBBDD();
            String consulta = "INSERT INTO Prestamo (dni_alumno, codigo_libro, fecha_prestamo) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta, PreparedStatement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, p.getAlumno().getDni());
            pstmt.setInt(2, p.getLibro().getCodigo());
            pstmt.setDate(3, Date.valueOf(p.getFecha_prestamo()));

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Obtener el ID generado
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);  // El primer campo es el ID generado
                }
                rs.close();
            }

            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return generatedId;  // Devuelve el ID generado o -1 si no se generó ningún ID
    }

    /**
     * Elimina un préstamo de la base de datos.
     *
     * @param p El objeto {@link ModeloPrestamo} que se desea eliminar, identificado por su ID.
     * @return {@code true} si la eliminación fue exitosa, {@code false} si ocurrió un error.
     */
    public static boolean eliminarPrestamo(ModeloPrestamo p) {
        ConexionBBDD connection;
        int resul = 0;
        try {
            connection = new ConexionBBDD();
            String consulta = "DELETE FROM Prestamo WHERE id_prestamo = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setInt(1, p.getId());

            resul = pstmt.executeUpdate();
            pstmt.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return resul > 0;
    }
}
