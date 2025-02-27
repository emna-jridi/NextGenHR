package controllers;

import entities.conge;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceConge;
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
    }

    @FXML
    private void handelSubmitAction() {
        String typeConge = typeCon.getValue();
        LocalDate dateDebut = datedep.getValue();
        LocalDate dateFin = datefin.getValue();
        String statusText = status.getText().trim();

        if (!validateInputs(typeConge, dateDebut, dateFin, statusText)) {
            return;
        }

        conge newConge = new conge(typeConge, dateDebut, dateFin, statusText);
        serviceConge.add(newConge);
        clearForm();
        System.out.println("Congé ajouté avec succès !");
    }


    private boolean validateInputs(String typeConge, LocalDate dateDebut, LocalDate dateFin, String statusText) {
        if (typeConge == null || dateDebut == null || dateFin == null || statusText.isEmpty()) {
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
        status.clear();
    }
}
