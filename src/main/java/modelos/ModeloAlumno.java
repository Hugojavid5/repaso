package modelos;

public class ModeloAlumno {

    private String dni;
    private String nombre;
    private String apellido1;
    private String apellido2;

    /**
     * Constructor con todos los atributos.
     *
     * @param dni       Documento Nacional de Identidad del alumno.
     * @param nombre    Nombre del alumno.
     * @param apellido1 Primer apellido del alumno.
     * @param apellido2 Segundo apellido del alumno.
     */
    public ModeloAlumno(String dni, String nombre, String apellido1, String apellido2) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido1 = apellido1;
        this.apellido2 = apellido2;
    }

    /**
     * Constructor vacío.
     */
    public ModeloAlumno() {
    }

    /**
     * Obtiene el DNI del alumno.
     *
     * @return DNI del alumno.
     */
    public String getDni() {
        return dni;
    }

    /**
     * Establece el DNI del alumno.
     *
     * @param dni DNI del alumno.
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Obtiene el nombre del alumno.
     *
     * @return Nombre del alumno.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del alumno.
     *
     * @param nombre Nombre del alumno.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el primer apellido del alumno.
     *
     * @return Primer apellido del alumno.
     */
    public String getApellido1() {
        return apellido1;
    }

    /**
     * Establece el primer apellido del alumno.
     *
     * @param apellido1 Primer apellido del alumno.
     */
    public void setApellido1(String apellido1) {
        this.apellido1 = apellido1;
    }

    /**
     * Obtiene el segundo apellido del alumno.
     *
     * @return Segundo apellido del alumno.
     */
    public String getApellido2() {
        return apellido2;
    }

    /**
     * Establece el segundo apellido del alumno.
     *
     * @param apellido2 Segundo apellido del alumno.
     */
    public void setApellido2(String apellido2) {
        this.apellido2 = apellido2;
    }

    /**
     * Devuelve una representación en cadena del alumno.
     *
     * @return Cadena con el formato "DNI - Nombre".
     */
    @Override
    public String toString() {
        return dni + " - " + nombre;
    }
}
