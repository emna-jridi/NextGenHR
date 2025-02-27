package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import tn.esprit.models.Teletravail;
import tn.esprit.services.ServiceTeletravail;

import java.net.URL;
import java.util.*;

public class TeletravailRHController implements javafx.fxml.Initializable {

    @FXML
    private Button IDApprouverTT, IDRefuserTT, IDSupprimerTT, IDafficherTT;
    @FXML
    private ComboBox<String> IDchoiceEmploye;
    @FXML
    private ListView<String> affichageid;

    private final ServiceTeletravail TT = new ServiceTeletravail();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateChoiceBox();
    }

    /**
     * Met à jour le ComboBox avec les employés ayant des demandes de télétravail "En attente".
     */
    private void updateChoiceBox() {
        List<Teletravail> teletravails = TT.getAll();
        IDchoiceEmploye.getItems().clear();

        Set<String> employeeEntries = new HashSet<>();
        for (Teletravail teletravail : teletravails) {
            if ("En attente".equals(teletravail.getStatutTT())) {
                String employeeName = TT.getEmployeeName(teletravail.getIdEmploye());
                if (employeeName != null && !employeeName.isEmpty()) {
                    String entry = employeeName + " - " + teletravail.getIdTeletravail();
                    employeeEntries.add(entry);
                }
            }
        }

        IDchoiceEmploye.getItems().addAll(employeeEntries);
    }

    /**
     * Affiche les demandes de télétravail pour l'employé sélectionné.
     */
    @FXML
    void AfficherTTbyName(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        affichageid.getItems().clear();

        if (selected == null || selected.trim().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un employé.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail demande = TT.getById(teletravailId);
        if (demande != null && "En attente".equals(demande.getStatutTT())) {
            String stats = TT.getEmployeeTTStats(demande.getIdEmploye());
            String info = String.format("Employé: %s (%s) | Date Demande: %s | Début: %s | Fin: %s | Statut: %s | Raison: %s",
                    demande.getIdTeletravail(), demande.getNomEmploye(), stats,
                    demande.getDateDemandeTT(), demande.getDateDebutTT(), demande.getDateFinTT(), demande.getStatutTT(), demande.getRaisonTT());
            affichageid.getItems().add(info);
        } else {
            affichageid.getItems().add("Aucune demande en attente pour cet employé.");
        }
    }

    /**
     * Approuve la demande sélectionnée.
     */
    @FXML
    void ApprouverTT(ActionEvent event) {
        processTeletravailAction("Approuvé");
    }

    /**
     * Refuse la demande sélectionnée.
     */
    @FXML
    void RefuserTT(ActionEvent event) {
        processTeletravailAction("Refusé");
    }

    /**
     * Supprime la demande sélectionnée.
     */
    @FXML
    void SupprimerTT(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à supprimer.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null && TT.delete(teletravailId)) {
            affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId));
            showAlert("Succès", "La demande a été supprimée.");
        } else {
            showAlert("Erreur", "La demande n'a pas pu être supprimée.");
        }

        updateChoiceBox();
    }

    /**
     * Traite l'action de validation (approuver/refuser) d'une demande et envoie un email.
     */
    private void processTeletravailAction(String statut) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande.");
            return;
        }

        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null) {
            selectedDemande.setStatutTT(statut);
            if (TT.update(selectedDemande)) {
                if ("Approuvé".equals(statut)) {
                    TT.incrementNbTTValide(selectedDemande.getIdEmploye());
                } else {
                    TT.incrementNbTTRefuse(selectedDemande.getIdEmploye());
                }

                // 🟢 Envoyer l'email
                TT.traiterDemandeTT(selectedDemande.getIdTeletravail(), statut);

                affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId));
                showAlert("Succès", "La demande a été " + statut.toLowerCase() + " et un email a été envoyé.");
            } else {
                showAlert("Erreur", "La demande n'a pas pu être " + statut.toLowerCase() + ".");
            }
            updateChoiceBox();
        } else {
            showAlert("Erreur", "La demande sélectionnée n'existe pas.");
        }
    }

    /**
     * Extrait l'identifiant du télétravail à partir de la chaîne de texte sélectionnée.
     */
    private int extractTeletravailId(String selected) {
        try {
            String[] parts = selected.split(" - ");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Affiche une alerte d'information.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
