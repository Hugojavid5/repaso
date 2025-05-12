package controladores;
import dao.DaoAlumno;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import modelos.ModeloAlumno;

public class ControllerAlumno {

    @FXML
    private TextField txtApellido1;

    @FXML
    private TextField txtApellido2;

    @FXML
    private TextField txtDNI;

    @FXML
    private TextField txtNombre;

    private ModeloAlumno alumno;

    private DaoAlumno daoAlumno;  // Instancia de DaoAlumno

    // Constructor para inicializar DaoAlumno con la conexión
    public ControllerAlumno(DaoAlumno daoAlumno) {
        this.daoAlumno = daoAlumno;
    }

    @FXML
    void accionGuardar(ActionEvent event) {
        String error = validadDatos();
        if (error.isEmpty()) {
            if (alumno == null) {
                // Crear Alumno
                if (daoAlumno.existeAlumnoPorDni(txtDNI.getText())) {
                    mostrarError("El DNI ya existe en la base de datos.");
                    return;
                }

                ModeloAlumno a = new ModeloAlumno(txtDNI.getText(), txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText());
                if (daoAlumno.insertarAlumno(a)) {
                    mostrarInfo("Alumno creado correctamente");
                    cerrarVentana();
                } else {
                    mostrarInfo("Error al crear el Alumno");
                }
            } else {
                // Modificar Alumno
                ModeloAlumno a = new ModeloAlumno(txtDNI.getText(), txtNombre.getText(), txtApellido1.getText(), txtApellido2.getText());
                if (daoAlumno.editarAlumno(a)) {
                    mostrarInfo("Alumno editado correctamente");
                    cerrarVentana();
                } else {
                    mostrarInfo("Error al editar el Alumno");
                }
            }
        } else {
            mostrarError(error);
        }
    }

    @FXML
    void accionCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    public String validadDatos() {
        String error = "";

        if (txtNombre.getText().isEmpty()) {
            error += "El campo 'Nombre' no puede estar vacio\n";
        }

        if (txtDNI.getText().isEmpty()) {
            error += "El campo 'DNI' no puede estar vacío\n";
        } else if (txtDNI.getText().length() > 9) {
            error += "El campo 'DNI' no puede tener más de 9 caracteres\n";
        }

        if (txtApellido1.getText().isEmpty()) {
            error += "El campo 'Apellido 1' no puede estar vacio\n";
        }

        if (txtApellido2.getText().isEmpty()) {
            error += "El campo 'Apellido 2' no puede estar vacio\n";
        }

        return error;
    }

    void mostrarError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(error);
        alert.showAndWait();
    }

    void mostrarInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setTitle("Info");
        alert.setContentText(info);
        alert.showAndWait();
    }

    public void setAlumno(ModeloAlumno a) {
        this.alumno = a;

        txtDNI.setText(alumno.getDni());
        txtNombre.setText(alumno.getNombre());
        txtApellido1.setText(alumno.getApellido1());
        txtApellido2.setText(alumno.getApellido2());

        txtDNI.setDisable(true);
    }
}
