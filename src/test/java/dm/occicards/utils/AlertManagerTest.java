package dm.occicards.utils;

import dm.occicards.model.Card;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertManagerTest {
    private AlertManager alertManager;

    @BeforeEach
    void setUp() {
        this.alertManager = new AlertManager("Title 1",
                "Header 1",
                "Message 1",
                Alert.AlertType.INFORMATION);
    }

    @Test
    void getTitle() {
        assertEquals("Title 1", this.alertManager.getTitle());
    }

    @Test
    void setTitle() {
        this.alertManager.setTitle("Title 2");
        assertEquals("Title 2", this.alertManager.getTitle());
    }

    @Test
    void getHeaderText() {
        assertEquals("Header 1", this.alertManager.getHeaderText());
    }

    @Test
    void setHeaderText() {
        this.alertManager.setHeaderText("Header 2");
        assertEquals("Header 2", this.alertManager.getHeaderText());
    }

    @Test
    void getMessage() {
        assertEquals("Message 1", this.alertManager.getMessage());
    }

    @Test
    void setMessage() {
        this.alertManager.setMessage("Message 2");
        assertEquals("Message 2", this.alertManager.getMessage());
    }

    @Test
    void getAlertType() {
        assertEquals(Alert.AlertType.INFORMATION, this.alertManager.getAlertType());
    }

    @Test
    void setAlertType() {
        this.alertManager.setAlertType(Alert.AlertType.ERROR);
        assertEquals(Alert.AlertType.ERROR, this.alertManager.getAlertType());
    }
}