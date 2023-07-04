module prova2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires java.sql;

    //opens prova2.controlechaves to javafx.fxml;
    opens prova2.controlechaves.controller to javafx.fxml, javafx.graphics;
    opens prova2.controlechaves.dao to javafx.fxml, javafx.graphics;
    opens prova2.controlechaves.dao.core to javafx.fxml, javafx.graphics;
    opens prova2.controlechaves.model to javafx.base, javafx.fxml, javafx.graphics;
    exports prova2;
}