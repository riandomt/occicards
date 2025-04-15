module dm.occicards {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;

    exports dm.occicards;
    opens dm.occicards.controller to javafx.fxml;
}
