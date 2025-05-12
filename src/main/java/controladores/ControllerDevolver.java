package controladores;

import dao.DaoHistorialPrestamo;
import dao.DaoLibro;
import dao.DaoPrestamo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import modelos.ModeloLibro;
import modelos.ModeloPrestamo;

/**
 * Controlador para gestionar la devolución de libros en la aplicación.
 * Permite cambiar el estado del libro al devolverlo y registrar la devolución en el historial.
 */
public class ControllerDevolver {

    /**
     * ComboBox para seleccionar el estado del libro.
     */
    @FXML
    private ComboBox<String> comboEstado;

    /**
     * ComboBox para seleccionar el préstamo que se desea devolver.
     */
    @FXML
    private ComboBox<ModeloPrestamo> comboPrestamo;

    /**
     * Maneja la acción de devolver un libro.
     * Verifica si los datos son válidos y, si es así, actualiza el estado del libro,
     * registra la devolución en el historial y guarda los cambios en la base de datos.
     *
     * @param event El evento de acción que invoca este metodo.
     */
    @FXML
    void accionDevolver(ActionEvent event) {
        String error=validadDatos();
        if (error.isEmpty()){
            ModeloPrestamo p=comboPrestamo.getSelectionModel().getSelectedItem();
            ModeloLibro l=p.getLibro();
            l.setEstado(comboEstado.getValue());
            if (DaoHistorialPrestamo.crearHistorico(p)){
                DaoLibro.editarLibro(l);
                mostrarInfo("Libro devuelvo correctamente");
                cerrarVentana();
            }else {
                mostrarInfo("Error al devolver el Prestamo");
            }
        }else {
            mostrarError(error);
        }
    }

    /**
     * Maneja la acción de cambiar el préstamo seleccionado en el ComboBox.
     * Cuando se cambia el préstamo, se actualiza el estado del libro correspondiente.
     *
     * @param event El evento de acción que invoca este metodo.
     */
    @FXML
    void accionCambiarPrestamo(ActionEvent event) {
        ModeloPrestamo p=comboPrestamo.getSelectionModel().getSelectedItem();
        // Verificar si no hay ningún préstamo seleccionado
        if (p == null) {
            comboEstado.setValue("Nuevo");
        } else {
            comboEstado.setValue(p.getLibro().getEstado());
        }
    }

    /**
     * Maneja la acción de cancelar la operación y cerrar la ventana.
     *
     * @param event El evento de acción que invoca este metodo.
     */
    @FXML
    void accionCancelar(ActionEvent event) {
        cerrarVentana();
    }

    /**
     * Metodo que valida los datos ingresados en los campos del formulario.
     * Verifica si los campos están vacíos o si el formato de los valores es incorrecto.
     *
     * @return Un mensaje de error si los datos son inválidos, o una cadena vacía si los datos son válidos.
     */
    public String validadDatos() {
        String error = "";

        // Verificar si el ComboBox tiene un valor seleccionado
        if (comboPrestamo.getValue() == null) {
            error += "Debe seleccionar un Prestamo en el campo 'Prestamo'\n";
        }
        if (comboEstado.getValue() == null) {
            error += "Debe seleccionar un Estado en el campo 'Estado'\n";
        }

        return error;
    }

    /**
     * Cierra la ventana actual del formulario de devolución.
     */
    private void cerrarVentana() {
        Stage stage = (Stage) comboEstado.getScene().getWindow();
        stage.close();
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
     * Inicializa los ComboBox con los valores de préstamos existentes y estados de libro.
     * También configura el estado del libro del préstamo seleccionado por defecto.
     */
    @FXML
    public void initialize() {
        // Cargar los valores a los ComboBox
        comboPrestamo.getItems().addAll(DaoPrestamo.todosPrestamos());
        //Elegir el primer item
        comboPrestamo.getSelectionModel().selectFirst();

        // Cargar los valores en el ComboBox
        comboEstado.getItems().addAll(
                "Nuevo",
                "Usado nuevo",
                "Usado seminuevo",
                "Usado estropeado",
                "Restaurado"
        );
        //Poner el estado del libro que tenia cuando se presto por defecto
        ModeloPrestamo p=comboPrestamo.getValue();
        comboEstado.setValue(p.getLibro().getEstado());
    }
}
