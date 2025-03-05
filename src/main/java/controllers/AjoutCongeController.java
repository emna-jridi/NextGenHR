package controllers;

import entities.conge;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import services.ServiceConge;

import java.io.IOException;
import java.time.LocalDate;


public class AjoutCongeController {

    @FXML
    private ComboBox<String> typeCon;

    @FXML
    private DatePicker datedep;

    @FXML
    private DatePicker datefin;

    @FXML
    private TextField status;

    @FXML
    private Button ajouterBtn;

    private ServiceConge serviceConge;

    public AjoutCongeController() {
        this.serviceConge = new ServiceConge();
    }

    @FXML
    public void initialize() {
        typeCon.getItems().addAll("Congé annuel", "Congé obligatoire", "Congé spécial", "Congé de maternité ou parental");
        status.setText("En attente"); // Définit "En attente" par défaut
    }



    @FXML
    private void handelSubmitAction() {
        String typeConge = typeCon.getValue();
        LocalDate dateDebut = datedep.getValue();
        LocalDate dateFin = datefin.getValue();
        String statusText = "En attente";

        if (!validateInputs(typeConge, dateDebut, dateFin)) {
            return;
        }

        conge newConge = new conge(typeConge, dateDebut, dateFin, statusText);
        serviceConge.add(newConge);

        showSuccessAlert();
        clearForm();

        // 🔹 Afficher immédiatement le congé ajouté

    }





    private boolean validateInputs(String typeConge, LocalDate dateDebut, LocalDate dateFin) {
        if (typeConge == null || dateDebut == null || dateFin == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis !");
            return false;
        }

        if (dateDebut.isAfter(dateFin)) {
            showAlert("Erreur", "La date de début ne peut pas être après la date de fin !");
            return false;
        }

        return true;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        typeCon.setValue(null);
        datedep.setValue(null);
        datefin.setValue(null);
    }
    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText("Congé ajouté avec succès !");
        alert.showAndWait();
    }

}
