module org.hugo.dein.repaso {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.sql.rowset;
    requires net.sf.jasperreports.core;
    requires javafx.web;


    opens org.hugo.dein.repaso to javafx.fxml;
    opens controladores to javafx.fxml;
    opens modelos to javafx.base;
    exports org.hugo.dein.repaso;


}