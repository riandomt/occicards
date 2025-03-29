package dm.occicards.model;

import dm.occicards.utils.AlertManager;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class Deck{
    private String name;
    private String description;
    private String revise;
    private Button edit;
    private Button delete;
    private Button download;

    public Deck(String name, String description,
                Button edit, Button delete, Button download) {
        this.setName(name);
        this.setDescription(description);
    }

    public Deck(String name, String description) {
        this.setName(name);
        this.setDescription(description);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRevise() {
        return revise;
    }

    public void setRevise(String revise) {
        this.revise = revise;
    }

    public Button getEdit() {
        return edit;
    }

    public void setEdit(Button edit) {
        this.edit = edit;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public Button getDownload() {
        return download;
    }

    public void setDownload(Button download) {
        this.download = download;
    }
}
