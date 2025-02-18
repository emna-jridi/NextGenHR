package tn.esprit.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.services.ServiceContrat;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ListContrats {

    @FXML
    private TableView<Contrat> tableView;
    @FXML
    private TableColumn<Contrat, Integer> colId;
    @FXML
    private TableColumn<Contrat, String> colType;
    @FXML
    private TableColumn<Contrat, String> colDateDebut;
    @FXML
    private TableColumn<Contrat, String> colDateFin;
    @FXML
    private TableColumn<Contrat, String> colStatut;

    @FXML
    private TableColumn<Contrat, Integer> colMontant;
    @FXML
    private TableColumn<Contrat, String> colNomClient;
    @FXML
    private TableColumn<Contrat, String> colEmailClient;
    @FXML
    private Button btnModifier;
    @FXML
    private Button btnSupprimer;

    private final ServiceContrat contratService = new ServiceContrat();
    private ObservableList<Contrat> contratList;

    @FXML
    public void initialize() {
        // Liaison des colonnes avec les propriétés de Contrat
        colId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdContrat()).asObject());
        colType.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTypeContrat()));
        colDateDebut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebutContrat().toString()));
        colDateFin.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFinContrat().toString()));
        colMontant.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getMontantContrat()).asObject());
        colNomClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNomClient()));
        colEmailClient.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmailClient()));
        colStatut.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusContrat()));

        loadContrats();
    }

    private void loadContrats() {
        List<Contrat> contrats = contratService.getAll();
        for (Contrat contrat : contrats) {
            // Assurez-vous que chaque contrat a un statut, sinon, vous pouvez lui assigner un statut par défaut
            if (contrat.getStatusContrat() == null) {
                contrat.setStatusContrat("Actif");  // Statut par défaut si non défini
            }
        }
        contratList = FXCollections.observableArrayList(contrats);
        tableView.setItems(contratList);
    }

    @FXML
    private void modifierContrat(ActionEvent event) {
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat à modifier.", Alert.AlertType.WARNING);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ModifierContrat.fxml"));
            Parent root = loader.load();

            ModifierContrat controller = loader.getController();
            controller.setContrat(contratSelectionne);

            Stage stage = new Stage();
            stage.setTitle("Modifier Contrat");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Rafraîchir la liste après modification
            loadContrats();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void supprimerContrat(ActionEvent event) {
        Contrat contratSelectionne = tableView.getSelectionModel().getSelectedItem();
        if (contratSelectionne == null) {
            showAlert("Aucun contrat sélectionné", "Veuillez sélectionner un contrat à supprimer.", Alert.AlertType.WARNING);
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Suppression du contrat");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer ce contrat ?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            contratService.delete(contratSelectionne.getIdContrat());
            loadContrats();
        }
    }



    @FXML
    private void ajouterContrat(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page AjouterContrat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterContrat.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène avec la page AjouterContrat
            Stage stage = new Stage();
            stage.setTitle("Ajouter un Contrat");
            stage.setScene(new Scene(root));
            stage.show();

            // Passer le contrôleur ListContrats à AjouterContrat
            AjouterContrat ajouterContratController = loader.getController();
            ajouterContratController.setOnContratAdded(() -> loadContrats()); // Callback pour actualiser la liste

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de l'ouverture de la page d'ajout.", Alert.AlertType.ERROR);
        }
    }



    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
