package controllers;

import entities.conge;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import services.ServiceConge;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ConsultationCongeController {

    @FXML
    private ListView<String> listViewConge;
    @FXML
    private TextField searchField;
    @FXML
    private Button btnAccepter;

    @FXML
    private Button btnRefuser;

    private ServiceConge serviceConge = new ServiceConge();
    private int selectedCongeId = -1;
    private ConsultationCongeController parentController;
    @FXML
    public void initialize() {
        handleAfficherAction(); // Afficher la liste des congés au chargement
    }
    private void configureListView() {

    }

    @FXML
    private void handleAfficherAction() {
        listViewConge.getItems().clear();
        for (conge c : serviceConge.getAll()) {
            listViewConge.getItems().add(formatConge(c));
        }
    }





    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase().trim();
        listViewConge.getItems().clear();
        for (conge c : serviceConge.getAll()) {
            String itemText = formatConge(c);
            if (itemText.toLowerCase().contains(searchText)) {
                listViewConge.getItems().add(itemText);
            }
        }
    }

    @FXML
    private void handleSortByDateDebutAction() {
        listViewConge.getItems().clear();
        List<conge> sortedConges = serviceConge.getAll()
                .stream()
                .sorted(Comparator.comparing(conge::getDate_debut))
                .toList();
        for (conge c : sortedConges) {
            listViewConge.getItems().add(formatConge(c));
        }
    }

    @FXML
    private void handleDeleteAction() {
        String selectedItem = listViewConge.getSelectionModel().getSelectedItem();
        if (selectedItem == null) {
            showAlert("Erreur", "Veuillez sélectionner un congé à supprimer !");
            return;
        }
        int id = extractId(selectedItem);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Voulez-vous vraiment supprimer ce congé ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            serviceConge.delete(id);
            handleAfficherAction();
        }
    }

    @FXML
    private void handleSelection() {
        String selectedItem = listViewConge.getSelectionModel().getSelectedItem();
        if (selectedItem == null) return;
        selectedCongeId = extractId(selectedItem);

        btnAccepter.setDisable(false);
        btnRefuser.setDisable(false);
    }




    @FXML
    private void handleStatistiqueAction() {
        List<conge> allConges = serviceConge.getAll();
        Map<String, Long> stats = allConges.stream()
                .collect(Collectors.groupingBy(conge::getType_conge, Collectors.counting()));

        PieChart pieChart = new PieChart();
        pieChart.setTitle("Répartition des Congés");
        for (Map.Entry<String, Long> entry : stats.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }

        VBox vbox = new VBox(pieChart);
        Stage stage = new Stage();
        stage.setTitle("Statistiques des Congés");
        stage.setScene(new Scene(vbox, 500, 400));
        stage.show();
    }

    private int extractId(String item) {
        return Integer.parseInt(item.split("\\|")[0].replace("ID: ", "").trim());

    }

    private String formatConge(conge c) {
        return "ID: " + c.getId() + " | Type: " + c.getType_conge() +
                " | Début: " + c.getDate_debut() + " | Fin: " + c.getDate_fin() +
                " | Statut: " + c.getStatus() + " | ";
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    private void handleUpdateAction() {
        if (selectedCongeId == -1) {
            showAlert("Erreur", "Veuillez sélectionner un congé à modifier !");
            return;
        }

        conge selectedConge = serviceConge.getById(selectedCongeId);
        if (selectedConge == null) {
            showAlert("Erreur", "Le congé sélectionné n'existe pas !");
            return;
        }

        navigateToModifier(selectedConge);
    }


    private void navigateToModifier(conge selectedConge) {
        try {
            System.out.println("Chargement de Modifier.fxml...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Modifier.fxml"));

            Parent root = loader.load();

            ModifierCongeController modifierController = loader.getController();
            if (modifierController == null) {
                System.out.println("Erreur : Impossible de récupérer le contrôleur ModifierCongeController.");
                return;
            }

            System.out.println("Envoi des données au contrôleur ModifierCongeController...");
            modifierController.setCongeData(selectedConge);

            Stage stage = new Stage();
            stage.setTitle("Modifier Congé");
            stage.setScene(new Scene(root));
            stage.show();

            System.out.println("Page Modifier.fxml affichée avec succès !");
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement de Modifier.fxml : " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void handleValidateConge(conge selectedConge) {
        updateStatus(selectedConge, "Validé");
    }

    private void handleInvalidateConge(conge selectedConge) {
        updateStatus(selectedConge, "Invalidé");
    }

    private void updateStatus(conge selectedConge, String newStatus) {
        selectedConge.setStatus(newStatus);
        serviceConge.update(selectedConge, selectedConge.getId());
        handleAfficherAction();
    }
    @FXML
    private void handleAccepterAction() {
        if (selectedCongeId != -1) {
            conge selectedConge = serviceConge.getById(selectedCongeId);
            if (selectedConge != null) {
                updateStatus(selectedConge, "Accepté");
            }
        }
    }

    @FXML
    private void handleRefuserAction() {
        if (selectedCongeId != -1) {
            conge selectedConge = serviceConge.getById(selectedCongeId);
            if (selectedConge != null) {
                updateStatus(selectedConge, "Refusé");
            }
        }
    }






}
