package tn.esprit.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import tn.esprit.services.OpenAIService;

import java.util.concurrent.CompletableFuture;

public class TestController {
    @FXML
    private TextArea questionArea;
    @FXML
    private Button generateButton;

    @FXML
    private void generateTest() {
        generateButton.setDisable(true);  // Disable button to prevent multiple clicks
        questionArea.setText("Generating question...");

        // Run OpenAI API call asynchronously
        CompletableFuture.supplyAsync(() -> {
            try {
                return OpenAIService.sendOpenAIRequest("Generate a multiple-choice test question about Java");
            } catch (Exception e) {
                e.printStackTrace();
                return "Error generating question!";
            }
        }).thenAccept(response -> {
            // Update UI on the JavaFX Application Thread
            javafx.application.Platform.runLater(() -> {
                questionArea.setText(response);
                generateButton.setDisable(false); // Re-enable button
            });
        });
    }
}
