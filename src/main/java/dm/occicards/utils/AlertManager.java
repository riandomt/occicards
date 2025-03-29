package dm.occicards.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

public class AlertManager {
    private String title;
    private String headerText;
    private String message;
    private Alert.AlertType alertType;

    public AlertManager(String title, String headerText, String message, Alert.AlertType alertType) {
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setMessage(message);
        this.setAlertType(alertType);
    }

    public void alert() {
        if (!this.getMessage().isEmpty()) {
            Alert alert = new Alert(this.getAlertType());
            alert.setTitle(this.getTitle() != null ? this.getTitle() : "Information");
            alert.setHeaderText(this.getHeaderText() != null ? this.getHeaderText() : "");
            alert.setContentText(this.getMessage() != null ? this.getMessage() : "Aucun message disponible.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    public boolean confirm() {
        if (!this.getMessage().isEmpty()) {
            Alert alert = new Alert(this.getAlertType());
            alert.setTitle(this.getTitle() != null ? this.getTitle() : "Confirmation");
            alert.setHeaderText(this.getHeaderText() != null ? this.getHeaderText() : "");
            alert.setContentText(this.getMessage() != null ? this.getMessage() : "");
            alert.setAlertType(Alert.AlertType.WARNING);
            alert.initModality(Modality.APPLICATION_MODAL);

            Optional<ButtonType> result = alert.showAndWait();
            return result.isPresent() && result.get() == ButtonType.OK;
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title != null ? title : "Information";
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText != null ? headerText : "";
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message != null ? message : "Aucun message disponible.";
    }

    public Alert.AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }
}
