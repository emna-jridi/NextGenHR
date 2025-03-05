package test;

import controllers.GoogleCalendarService;
import controllers.ReunionController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CongeMain extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        try {

            DBConnection db = DBConnection.getInstance();


            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ConsultationConge .fxml"));
            ReunionController reunionController = new ReunionController();
            reunionController.planifierReunion("Réunion d'équipe", "Discussion sur les mises à jour du projet.");

            Parent root = loader.load();

            Scene scene = new Scene(root);
            stage.setTitle("Gestion reunion");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
