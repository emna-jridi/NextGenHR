package tn.esprit.Test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainCandidatureFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainView.fxml"));
            try {
                Parent root = loader.load();
                Scene scene = new Scene(root);
                primaryStage.setScene(scene);
                primaryStage.setTitle("---- Gestion Offres -----");
                primaryStage.show();

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }


    }

    }

