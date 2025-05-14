package dao;

import BBDD.ConexionBBDD;
import modelos.ModeloAlumno;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoAlumno {
    private static Connection conexion;

    public DaoAlumno(Connection conexion) {
        this.conexion = conexion;
    }

    /**
     * Obtiene todos los alumnos de la base de datos.
     */
    // Metodo estático que recibe la conexión como parámetro
    public static List<ModeloAlumno> obtenerTodosLosAlumnos(Connection conexion) {
        List<ModeloAlumno> lista = new ArrayList<>();
        String sql = "SELECT * FROM Alumno";

        try (Statement stmt = conexion.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ModeloAlumno alumno = new ModeloAlumno(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellido1"),
                        rs.getString("apellido2")
                );
                lista.add(alumno);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener todos los alumnos: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene un alumno por su DNI.
     */
    public static ModeloAlumno obtenerAlumnoPorDni(String dni_alumno) {
        ConexionBBDD connection;
        ModeloAlumno alumno = null;
        try {
            connection = new ConexionBBDD();
            String consulta = "SELECT dni,nombre,apellido1,apellido2 FROM Alumno WHERE dni = ?";
            PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
            pstmt.setString(1, dni_alumno);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String dni = rs.getString(1);
                String nombre = rs.getString(2);
                String apellido1 = rs.getString(3);
                String apellido2 = rs.getString(4);
                alumno = new ModeloAlumno(dni,nombre,apellido1,apellido2);
            }
            rs.close();
            connection.CloseConexion();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return alumno;
    }

    /**
     * Inserta un nuevo alumno.
     */
    public boolean insertarAlumno(ModeloAlumno alumno) {
        String sql = "INSERT INTO Alumno (dni, nombre, apellido1, apellido2) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, alumno.getDni());
            stmt.setString(2, alumno.getNombre());
            stmt.setString(3, alumno.getApellido1());
            stmt.setString(4, alumno.getApellido2());

            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error al insertar alumno: " + e.getMessage());
            return false;
        }
    }

    /**
     * Edita los datos de un alumno existente.
     */
    public boolean editarAlumno(ModeloAlumno alumno) {
        String sql = "UPDATE Alumno SET nombre = ?, apellido1 = ?, apellido2 = ? WHERE dni = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, alumno.getNombre());
            stmt.setString(2, alumno.getApellido1());
            stmt.setString(3, alumno.getApellido2());
            stmt.setString(4, alumno.getDni());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al editar alumno: " + e.getMessage());
            return false;
        }
    }

    /**
     * Comprueba si un alumno existe por su DNI.
     */
    public boolean existeAlumnoPorDni(String dni) {
        String sql = "SELECT COUNT(*) FROM Alumno WHERE dni = ?";

        try (PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al comprobar existencia del alumno: " + e.getMessage());
        }

        return false;
    }
}
