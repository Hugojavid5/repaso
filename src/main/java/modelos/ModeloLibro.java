package modelos;

import java.sql.Blob;

public class ModeloLibro {
        private int codigo;
        private String titulo;
        private String autor;
        private String editorial;
        private String estado;
        private int baja;
        private Blob foto;

        /**
         * Constructor con todos los atributos.
         *
         * @param codigo    Código único del libro.
         * @param titulo    Título del libro.
         * @param autor     Autor del libro.
         * @param editorial Editorial del libro.
         * @param estado    Estado del libro (ej. disponible, prestado, deteriorado).
         * @param baja      Indica si el libro está dado de baja (1) o activo (0).
         * @param foto      Imagen asociada al libro.
         */
        public ModeloLibro(int codigo, String titulo, String autor, String editorial, String estado, int baja, Blob foto) {
            this.codigo = codigo;
            this.titulo = titulo;
            this.autor = autor;
            this.editorial = editorial;
            this.estado = estado;
            this.baja = baja;
            this.foto = foto;
        }

        /**
         * Constructor vacío.
         */
        public ModeloLibro() {
        }

        /**
         * Obtiene el código del libro.
         *
         * @return Código único del libro.
         */
        public int getCodigo() {
            return codigo;
        }

        /**
         * Establece el código del libro.
         *
         * @param codigo Código único del libro.
         */
        public void setCodigo(int codigo) {
            this.codigo = codigo;
        }

        /**
         * Obtiene el título del libro.
         *
         * @return Título del libro.
         */
        public String getTitulo() {
            return titulo;
        }

        /**
         * Establece el título del libro.
         *
         * @param titulo Título del libro.
         */
        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        /**
         * Obtiene el autor del libro.
         *
         * @return Autor del libro.
         */
        public String getAutor() {
            return autor;
        }

        /**
         * Establece el autor del libro.
         *
         * @param autor Autor del libro.
         */
        public void setAutor(String autor) {
            this.autor = autor;
        }

        /**
         * Obtiene la editorial del libro.
         *
         * @return Editorial del libro.
         */
        public String getEditorial() {
            return editorial;
        }

        /**
         * Establece la editorial del libro.
         *
         * @param editorial Editorial del libro.
         */
        public void setEditorial(String editorial) {
            this.editorial = editorial;
        }

        /**
         * Obtiene el estado del libro.
         *
         * @return Estado del libro (ej. disponible, prestado, deteriorado).
         */
        public String getEstado() {
            return estado;
        }

        /**
         * Establece el estado del libro.
         *
         * @param estado Estado del libro.
         */
        public void setEstado(String estado) {
            this.estado = estado;
        }

        /**
         * Obtiene el estado de baja del libro.
         *
         * @return 1 si el libro está dado de baja, 0 si está activo.
         */
        public int getBaja() {
            return baja;
        }

        /**
         * Establece el estado de baja del libro.
         *
         * @param baja 1 si el libro está dado de baja, 0 si está activo.
         */
        public void setBaja(int baja) {
            this.baja = baja;
        }

        /**
         * Obtiene la imagen asociada al libro.
         *
         * @return Imagen en formato Blob.
         */
        public Blob getFoto() {
            return foto;
        }

        /**
         * Establece la imagen asociada al libro.
         *
         * @param foto Imagen en formato Blob.
         */
        public void setFoto(Blob foto) {
            this.foto = foto;
        }

        /**
         * Devuelve una representación en cadena del libro.
         *
         * @return Cadena con el formato "Código - Título".
         */
        @Override
        public String toString() {
            return codigo + " - " + titulo;
        }
    }
