package dm.occicards.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;

import java.util.Optional;

/**
 * Utility class for managing and displaying alerts in the application.
 */
public class AlertManager {
    private String title;
    private String headerText;
    private String message;
    private Alert.AlertType alertType;

    /**
     * Constructs a new AlertManager with the specified title, header text, message, and alert type.
     *
     * @param title      The title of the alert.
     * @param headerText The header text of the alert.
     * @param message    The message content of the alert.
     * @param alertType  The type of alert (e.g., information, warning, error).
     */
    public AlertManager(String title, String headerText, String message, Alert.AlertType alertType) {
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setMessage(message);
        this.setAlertType(alertType);
    }

    /**
     * Displays an alert dialog with the specified properties.
     */
    public void alert() {
        if (!this.getMessage().isEmpty()) {
            Alert alert = new Alert(this.getAlertType());
            alert.setTitle(this.getTitle() != null ? this.getTitle() : "Information");
            alert.setHeaderText(this.getHeaderText() != null ? this.getHeaderText() : "");
            alert.setContentText(this.getMessage() != null ? this.getMessage() : "No message available.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.showAndWait();
        }
    }

    /**
     * Displays a confirmation dialog and returns true if the user confirms the action.
     *
     * @return True if the user confirms the action, false otherwise.
     */
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

    /**
     * Gets the title of the alert.
     *
     * @return The title of the alert.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the alert.
     *
     * @param title The title to set.
     */
    public void setTitle(String title) {
        this.title = title != null ? title : "Information";
    }

    /**
     * Gets the header text of the alert.
     *
     * @return The header text of the alert.
     */
    public String getHeaderText() {
        return headerText;
    }

    /**
     * Sets the header text of the alert.
     *
     * @param headerText The header text to set.
     */
    public void setHeaderText(String headerText) {
        this.headerText = headerText != null ? headerText : "";
    }

    /**
     * Gets the message content of the alert.
     *
     * @return The message content of the alert.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message content of the alert.
     *
     * @param message The message content to set.
     */
    public void setMessage(String message) {
        this.message = message != null ? message : "No message available.";
    }

    /**
     * Gets the type of the alert.
     *
     * @return The type of the alert.
     */
    public Alert.AlertType getAlertType() {
        return alertType;
    }

    /**
     * Sets the type of the alert.
     *
     * @param alertType The type of alert to set.
     */
    public void setAlertType(Alert.AlertType alertType) {
        this.alertType = alertType;
    }
}
