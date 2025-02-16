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

    @FXML private ListView<Teletravail> IdList; // Liste des demandes de télétravail
    @FXML private DatePicker Id_Date_Fin;
    @FXML private TextField Id_Identifiant;
    @FXML private TextArea Id_Raison;
    @FXML private DatePicker Id_date_debut;

    private ServiceTeletravail TTService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TTService = new ServiceTeletravail();

        // Définir la façon d'afficher les éléments dans la ListView
        IdList.setCellFactory(param -> new ListCell<Teletravail>() {
            @Override
            protected void updateItem(Teletravail tt, boolean empty) {
                super.updateItem(tt, empty);
                if (empty || tt == null) {
                    setText(null);
                } else {
                    setText(formatTeletravail(tt)); // Affichage formaté de la demande
                }
            }
        });

        // Ajouter un écouteur de sélection d'élément
        IdList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                remplirChamps(newValue);
            }
        });
    }

    // Affiche les demandes de télétravail pour un employé donné
    @FXML
    void AfficherTT(ActionEvent event) {
        String identifiant = Id_Identifiant.getText().trim();

        if (identifiant.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un identifiant valide.");
            return;
        }

        int idEmploye = parseId(identifiant);
        if (idEmploye == -1) return;

        List<Teletravail> demandes = TTService.getTeletravailByEmploye(idEmploye);
        if (demandes.isEmpty()) {
            showAlert("Info", "Aucune demande trouvée pour cet employé.");
            IdList.getItems().clear();
        } else {
            IdList.getItems().setAll(demandes);
        }
    }

    // Modifier une demande de télétravail
    @FXML
    void ModifierTT(ActionEvent event) {
        if (!validateFields()) return;

        Teletravail teletravail = IdList.getSelectionModel().getSelectedItem();
        if (teletravail == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à modifier.");
            return;
        }

        // Mettre à jour l'objet avec les nouvelles valeurs
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDebutTT(Id_date_debut.getValue());
        teletravail.setDateFinTT(Id_Date_Fin.getValue());

        if (TTService.update(teletravail)) {
            showAlert("Succès", "Demande de télétravail modifiée avec succès.");
            afficherTTbyId(new ActionEvent()); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Erreur lors de la modification de la demande.");
        }
    }

    // Ajouter une nouvelle demande de télétravail
    @FXML
    void AjouterTT(ActionEvent event) {
        if (!validateFields()) return;

        int idEmploye = parseId(Id_Identifiant.getText().trim());
        if (idEmploye == -1) return;

        Teletravail teletravail = new Teletravail();
        teletravail.setIdEmploye(idEmploye);
        teletravail.setRaisonTT(Id_Raison.getText().trim());
        teletravail.setDateDemandeTT(LocalDate.now());
        teletravail.setDateDebutTT(Id_date_debut.getValue());
        teletravail.setDateFinTT(Id_Date_Fin.getValue());
        teletravail.setStatutTT("En attente");

        if (TTService.add(teletravail)) {
            showAlert("Succès", "Demande de télétravail ajoutée avec succès.");
            afficherTTbyId(new ActionEvent()); // Rafraîchir la liste
        } else {
            showAlert("Erreur", "Erreur lors de l'ajout de la demande.");
        }
    }

    // Remplir les champs avec les données de l'objet Teletravail sélectionné
    private void remplirChamps(Teletravail teletravail) {
        Id_Identifiant.setText(String.valueOf(teletravail.getIdEmploye()));
        Id_Raison.setText(teletravail.getRaisonTT());
        Id_date_debut.setValue(teletravail.getDateDebutTT());
        Id_Date_Fin.setValue(teletravail.getDateFinTT());
    }

    // Afficher une alerte d'information
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Récupérer les demandes de télétravail d'un employé par ID
    @FXML
    void afficherTTbyId(ActionEvent event) {
        String identifiant = Id_Identifiant.getText().trim();

        if (identifiant.isEmpty()) {
            showAlert("Erreur", "Veuillez entrer un identifiant valide.");
            return;
        }

        int idEmploye = parseId(identifiant);
        if (idEmploye == -1) return;

        List<Teletravail> demandes = TTService.getTeletravailByEmploye(idEmploye);
        if (demandes.isEmpty()) {
            showAlert("Info", "Aucune demande trouvée pour cet employé.");
            IdList.getItems().clear();
        } else {
            IdList.getItems().setAll(demandes);
        }
    }

    // Fonction utilitaire pour valider les champs du formulaire
    private boolean validateFields() {
        if (Id_Identifiant.getText().trim().isEmpty() || Id_Raison.getText().trim().isEmpty() ||
                Id_date_debut.getValue() == null || Id_Date_Fin.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    // Fonction utilitaire pour parser l'identifiant de l'employé
    private int parseId(String identifiant) {
        try {
            return Integer.parseInt(identifiant);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'identifiant doit être un nombre.");
            return -1;
        }
    }

    // Retourne un format texte de l'objet Teletravail pour l'affichage dans la ListView
    private String formatTeletravail(Teletravail tt) {
        return "ID: " + tt.getIdTeletravail() + " | " + tt.getRaisonTT() + " (" +
                tt.getDateDebutTT() + " - " + tt.getDateFinTT() + ")" + "|" + tt.getStatutTT();
    }
}
