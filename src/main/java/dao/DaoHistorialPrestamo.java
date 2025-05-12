package dao;

import BBDD.ConexionBBDD;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import modelos.ModeloAlumno;
import modelos.ModeloHistorialPrestamo;
import modelos.ModeloLibro;
import modelos.ModeloPrestamo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class DaoHistorialPrestamo {
        /**
         * Obtiene todos los registros del histórico de préstamos.
         *
         * @return Una lista observable con todos los registros del histórico.
         */
        public static ObservableList<ModeloHistorialPrestamo> todosHistoricos() {
            ConexionBBDD connection;
            ObservableList<ModeloHistorialPrestamo> historicos = FXCollections.observableArrayList();
            try {
                connection = new ConexionBBDD();
                String consulta = "SELECT id_prestamo,dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion FROM Historico_prestamo";
                PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    int id_prestamo = rs.getInt(1);
                    String dni_alumno = rs.getString(2);
                    String codigo_libro = rs.getString(3);
                    LocalDate fecha_prestamo = rs.getDate(4).toLocalDate();
                    LocalDate fecha_devolucion = rs.getDate(5).toLocalDate();

                    ModeloAlumno a= DaoAlumno.obtenerAlumnoPorDni(dni_alumno);
                    ModeloLibro l= DaoLibro.LibroID(codigo_libro);

                    ModeloHistorialPrestamo h =new ModeloHistorialPrestamo(id_prestamo,a,l,fecha_prestamo,fecha_devolucion);
                    historicos.add(h);
                }
                rs.close();
                connection.CloseConexion();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
            return historicos;
        }

        /**
         * Registra un préstamo en el histórico y lo elimina de la lista de préstamos activos.
         *
         * @param p El préstamo que se desea registrar en el histórico.
         * @return true si la operación fue exitosa, false en caso contrario.
         */
        public static boolean crearHistorico(ModeloPrestamo p) {
            ConexionBBDD connection;
            int resul = 0;
            try {
                connection = new ConexionBBDD();
                String consulta = "INSERT INTO Historico_prestamo (dni_alumno,codigo_libro,fecha_prestamo,fecha_devolucion) VALUES (?,?,?,?) ";
                PreparedStatement pstmt = connection.getConnection().prepareStatement(consulta);
                pstmt.setString(1, p.getAlumno().getDni());
                pstmt.setInt(2, p.getLibro().getCodigo());
                // Fecha LocalDate
                pstmt.setDate(3, Date.valueOf(p.getFecha_prestamo()));
                LocalDate fechaHoy = LocalDate.now();
                pstmt.setDate(4, Date.valueOf(fechaHoy));

                resul = pstmt.executeUpdate();
                pstmt.close();
                connection.CloseConexion();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DaoPrestamo.eliminarPrestamo(p);
            return resul > 0;
        }
    }

