
package tn.esprit.models;
import java.util.List;
import java.util.ArrayList;

public class Question {
    private int id;
    private String texte;
    private String type; // "CHECKBOX", "COMBOBOX", "TEXT"
    private List<String> options; // Store options as comma-separated string in DB
    private String correct_response;
    private int testId;
    private int points;


    public Question() {
        this.options = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTexte() { return texte; }
    public void setTexte(String texte) { this.texte = texte; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public List<String> getOptions() { return options; }
    public void setOptions(List<String> options) { this.options = options; }

    public String getCorrectResponse() { return correct_response; }
    public void setCorrectResponse(String correctResponse) { this.correct_response = correctResponse; }

    public int getTestId() { return testId; }
    public void setTestId(int testId) { this.testId = testId; }

    public int getPoints() { return points; }
    public void setPoints(int points) { this.points = points; }

    // Helper method to convert options list to string for DB storage
    public String getOptionsAsString() {
        return String.join(",", options);
    }

    // Helper method to set options from DB string
    public void setOptionsFromString(String optionsStr) {
        if (optionsStr != null && !optionsStr.isEmpty()) {
            this.options = new ArrayList<>(List.of(optionsStr.split(",")));
        }
    }
}
