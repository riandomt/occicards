module dm.occicards {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.json;

    opens dm.occicards.controller to javafx.fxml;
    exports dm.occicards;
}
