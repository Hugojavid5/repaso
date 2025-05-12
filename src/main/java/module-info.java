module org.hugo.dein.repaso {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires net.sf.jasperreports.core;
    requires javafx.web;


    opens org.hugo.dein.repaso to javafx.fxml;
    exports org.hugo.dein.repaso;
    exports controladores;
    opens controladores to javafx.fxml;
}