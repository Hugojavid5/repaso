module org.hugo.dein.repaso {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.hugo.dein.repaso to javafx.fxml;
    exports org.hugo.dein.repaso;
}