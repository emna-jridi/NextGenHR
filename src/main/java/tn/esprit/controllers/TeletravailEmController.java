package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceTeletravail;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TeletravailEmController implements Initializable {
    @FXML
    private ListView<Teletravail> IdList; // Modifié pour stocker des objets Teletravail

    @FXML
    private DatePicker Id_Date_Fin;

    @FXML
    private TextField Id_Identifiant;

    @FXML
    private TextArea Id_Raison;

    @FXML
    private DatePicker Id_date_debut;

    private ServiceTeletravail TTService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTService = new ServiceTeletravail();

        // Définir comment afficher les éléments dans la ListView
        IdList.setCellFactory(param -> new ListCell<Teletravail>() {
            @Override
            protected void updateItem(Teletravail tt, boolean empty) {
                super.updateItem(tt, empty);
                if (empty || tt == null) {
                    setText(null);
                } else {
                    setText("ID: " + tt.getIdTeletravail() + " | " + tt.getRaisonTT() + " (" +
                            tt.getDateDebutTT() + " - " + tt.getDateFinTT() + ")" + "|" + tt.getStatutTT());
                }
            }
        });

        // Écouteur pour détecter la sélection
        IdList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChamps(newValue);
            }
        });
    }
    @FXML
    void AfficherTT(ActionEvent event) {
        String identifiant = Id_Identifiant.getText().trim();

        if (identifiant.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un identifiant valide.");
            return;
        }

        int idEmploye;
        try {
            idEmploye = Integer.parseInt(identifiant);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'identifiant doit être un nombre.");
            return;
        }

        List<Teletravail> demandes = TTService.getTeletravailByEmploye(idEmploye);

        if (demandes.isEmpty()) {
            showAlert("Info", "Aucune demande trouvée pour cet employé.");
            IdList.getItems().clear();
        } else {
            IdList.getItems().setAll(demandes);
        }
    }

    @FXML
    void ModifierTT(ActionEvent event) {
        Teletravail teletravail = IdList.getSelectionModel().getSelectedItem();

        if (teletravail == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à modifier.");
            return;
        }

        // Mettre à jour l'objet avec les nouvelles valeurs
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDebutTT(Id_date_debut.getValue());
        teletravail.setDateFinTT(Id_Date_Fin.getValue());

        boolean isUpdated = TTService.update(teletravail);

        if (isUpdated) {
            showAlert("Succès", "Demande de télétravail modifiée avec succès.");
            // Mettre à jour la liste
            afficherTTbyId(new ActionEvent());
        } else {
            showAlert("Erreur", "Erreur lors de la modification de la demande.");
        }
    }

    @FXML
    void AjouterTT(ActionEvent event) {
        if (Id_Identifiant.getText().trim().isEmpty() || Id_Raison.getText().trim().isEmpty() ||
                Id_date_debut.getValue() == null || Id_Date_Fin.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        int idEmploye;
        try {
            idEmploye = Integer.parseInt(Id_Identifiant.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'identifiant doit être un nombre.");
            return;
        }

        Teletravail teletravail = new Teletravail();
        teletravail.setIdEmploye(idEmploye);
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDemandeTT(LocalDate.now());
        teletravail.setDateDebutTT(Id_date_debut.getValue());
        teletravail.setDateFinTT(Id_Date_Fin.getValue());
        teletravail.setStatutTT("En attente");

        boolean isAdded = TTService.add(teletravail);

        if (isAdded) {
            showAlert("Succès", "Demande de télétravail ajoutée avec succès.");
            afficherTTbyId(new ActionEvent()); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout de la demande.");
        }
    }

    private void remplirChamps(Teletravail teletravail) {
        Id_Identifiant.setText(String.valueOf(teletravail.getIdEmploye()));
        Id_Raison.setText(teletravail.getRaisonTT());
        Id_date_debut.setValue(teletravail.getDateDebutTT());
        Id_Date_Fin.setValue(teletravail.getDateFinTT());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void afficherTTbyId(ActionEvent event) {
        String identifiant = Id_Identifiant.getText().trim();

        if (identifiant.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un identifiant valide.");
            return;
        }

        int idEmploye;
        try {
            idEmploye = Integer.parseInt(identifiant);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'identifiant doit être un nombre.");
            return;
        }

        // Récupérer les demandes de télétravail
        List<Teletravail> demandes = TTService.getTeletravailByEmploye(idEmploye);

        if (demandes.isEmpty()) {
            showAlert("Info", "Aucune demande trouvée pour cet employé.");
            IdList.getItems().clear();
        } else {
            IdList.getItems().setAll(demandes); // On ajoute directement les objets Teletravail
        }
    }

    @FXML
    void remplirChampsDepuisListe() {
        Teletravail selectedTeletravail = IdList.getSelectionModel().getSelectedItem();

        if (selectedTeletravail == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande de télétravail.");
            return;
        }

        // Remplir les champs avec les valeurs de l'objet sélectionné
        Id_Identifiant.setText(String.valueOf(selectedTeletravail.getIdEmploye()));
        Id_Raison.setText(selectedTeletravail.getRaisonTT());
        Id_date_debut.setValue(selectedTeletravail.getDateDebutTT());
        Id_Date_Fin.setValue(selectedTeletravail.getDateFinTT());
    }

}
