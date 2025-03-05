package tn.esprit.test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("Démarrage de l'application...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ListeContrats.fxml"));
        try {
            Parent root = loader.load();
            System.out.println("FXML chargé avec succès.");
            Scene scene = new Scene(root);
            primaryStage.setTitle("gestion Ressources Humaines ");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement du FXML : " + e.getMessage());
            e.printStackTrace();
        }
    }
}