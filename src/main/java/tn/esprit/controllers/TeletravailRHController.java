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
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TeletravailRHController implements javafx.fxml.Initializable {

    @FXML
    private Button IDApprouverTT;
    @FXML
    private Button IDRefuserTT;
    @FXML
    private Button IDSupprimerTT;
    @FXML
    private Button IDafficherTT;
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
     * Met à jour le ComboBox en affichant uniquement les employés ayant des demandes de télétravail.
     */
    private void updateChoiceBox() {
        List<Teletravail> teletravails = TT.getAll();
        IDchoiceEmploye.getItems().clear();

        Set<String> employeeEntries = new HashSet<>();
        for (Teletravail teletravail : teletravails) {
            // Vérifier que l'employé a une demande en attente de validation
            if ("En attente".equals(teletravail.getStatutTT())) {
                String employeeName = TT.getEmployeeName(teletravail.getIdEmploye());
                String entry = employeeName + " - " + teletravail.getIdTeletravail(); // Affichage du nom et de l'ID du télétravail

                if (!employeeEntries.contains(entry)) {
                    employeeEntries.add(entry);
                    IDchoiceEmploye.getItems().add(entry);
                }
            }
        }
    }

    /**
     * Affiche les demandes de télétravail pour l'employé sélectionné.
     */
    @FXML
    void AfficherTTbyName(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        affichageid.getItems().clear();

        if (selected == null || selected.trim().isEmpty()) {
            showAlert("Erreur", "Veuillez sélectionner un employé dans le ComboBox.");
            return;
        }

        // Extraire l'ID du télétravail à partir de la chaîne du ComboBox
        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }

        // Récupérer toutes les demandes de télétravail pour l'employé sélectionné
        List<Teletravail> allTeletravails = TT.getAll();
        boolean found = false;
        for (Teletravail t : allTeletravails) {
            // Si l'employé a une demande et correspond à l'ID sélectionné
            if (t.getIdTeletravail() == teletravailId && "En attente".equals(t.getStatutTT())) {
                String stats = TT.getEmployeeTTStats(t.getIdEmploye());
                String info = "ID: " + t.getIdTeletravail() +
                        " | Employé: " + t.getNomEmploye() + " (" + stats + ")" +
                        " | Date Demande: " + t.getDateDemandeTT() +
                        " | Début: " + t.getDateDebutTT() +
                        " | Fin: " + t.getDateFinTT() +
                        " | Statut: " + t.getStatutTT() +
                        " | Raison: " + t.getRaisonTT();
                affichageid.getItems().add(info);
                found = true;
            }
        }

        if (!found) {
            affichageid.getItems().add("Aucune demande trouvée pour l'employé sélectionné.");
        }
    }

    /**
     * Approuve la demande sélectionnée.
     */
    @FXML
    void ApprouverTT(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à approuver.");
            return;
        }
        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }
        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null) {
            selectedDemande.setStatutTT("Approuvé");
            if (TT.update(selectedDemande)) {
                TT.incrementNbTTValide(selectedDemande.getIdEmploye());
                affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId + ","));
                showAlert("Succès", "La demande a été approuvée.");
            } else {
                showAlert("Erreur", "La demande n'a pas pu être approuvée.");
            }
            updateChoiceBox();
        } else {
            showAlert("Erreur", "La demande sélectionnée n'existe pas.");
        }
    }

    /**
     * Refuse la demande sélectionnée.
     */
    @FXML
    void RefuserTT(ActionEvent event) {
        String selected = IDchoiceEmploye.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Erreur", "Veuillez sélectionner une demande à refuser.");
            return;
        }
        int teletravailId = extractTeletravailId(selected);
        if (teletravailId == -1) {
            showAlert("Erreur", "Format de sélection invalide.");
            return;
        }
        Teletravail selectedDemande = TT.getById(teletravailId);
        if (selectedDemande != null) {
            selectedDemande.setStatutTT("Refusé");
            if (TT.update(selectedDemande)) {
                TT.incrementNbTTRefuse(selectedDemande.getIdEmploye());
                affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId + ","));
                showAlert("Succès", "La demande a été refusée.");
            } else {
                showAlert("Erreur", "La demande n'a pas pu être refusée.");
            }
            updateChoiceBox();
        } else {
            showAlert("Erreur", "La demande sélectionnée n'existe pas.");
        }
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
        if (selectedDemande != null) {
            if (TT.delete(selectedDemande.getIdTeletravail())) {
                affichageid.getItems().removeIf(item -> item.contains("ID: " + teletravailId + ","));
                showAlert("Succès", "La demande a été supprimée.");
            } else {
                showAlert("Erreur", "La demande n'a pas pu être supprimée.");
            }
            updateChoiceBox();
        } else {
            showAlert("Erreur", "La demande sélectionnée n'existe pas.");
        }
    }

    /**
     * Extrait l'identifiant de la demande à partir de la chaîne du ComboBox.
     * Le format attendu est "Nom de l'employé - idTeletravail".
     *
     * @param selected la chaîne sélectionnée
     * @return l'identifiant sous forme d'entier ou -1 en cas d'erreur
     */
    private int extractTeletravailId(String selected) {
        try {
            String[] parts = selected.split(" - "); // Le format attendu est "Nom - ID"
            return Integer.parseInt(parts[1]); // Récupérer l'ID du télétravail
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Affiche une alerte d'information.
     *
     * @param title   le titre de l'alerte
     * @param message le message à afficher
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
