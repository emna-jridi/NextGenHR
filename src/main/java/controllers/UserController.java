package controllers;
import entities.User;
import entities.User.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import services.ServiceUser;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
public class UserController {
    @FXML
    private TableView<User> tableViewUsers;

    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colNom;
    @FXML
    private TableColumn<User, String> colPrenom;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, Role> colRole;
    @FXML
    private TableColumn<User, LocalDate> colDateInscription;

    @FXML
    private TextField txtRecherche;

    @FXML
    private Button btnAjouter, btnModifier, btnSupprimer, btnReinitialiser;

    private ServiceUser serviceUser = new ServiceUser();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        // Configuration des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("idUser"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nomUser"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenomUser"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailUser"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));


        // Charger les utilisateurs dans le tableau
        loadUsers();

        // Recherche dynamique
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrerUtilisateurs(newValue);
        });
    }

    private void loadUsers() {
        List<User> users = serviceUser.getAll();
        userList = FXCollections.observableArrayList(users);
        tableViewUsers.setItems(userList);
    }

    private void filtrerUtilisateurs(String recherche) {
        List<User> filteredList = userList.stream()
                .filter(user -> user.getNomUser().toLowerCase().contains(recherche.toLowerCase()) ||
                        user.getPrenomUser().toLowerCase().contains(recherche.toLowerCase()) ||
                        user.getEmailUser().toLowerCase().contains(recherche.toLowerCase()))
                .collect(Collectors.toList());

        tableViewUsers.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void ajouterUtilisateur(ActionEvent event) {
        // Logique pour ajouter un utilisateur
        // Ouvre une nouvelle fenêtre ou formulaire pour saisir les informations
    }

    @FXML
    private void modifierUtilisateur(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Logique pour modifier l'utilisateur sélectionné
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur à modifier.");
        }
    }

    @FXML
    private void supprimerUtilisateur(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            serviceUser.delete(selectedUser.getIdUser());
            loadUsers();
            showAlert("Suppression", "Utilisateur supprimé avec succès.");
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur à supprimer.");
        }
    }

    @FXML
    private void reinitialiserMotDePasse(ActionEvent event) {
        User selectedUser = tableViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setPassword("password123"); // Réinitialisation simple
            serviceUser.update(selectedUser);
            showAlert("Réinitialisation", "Mot de passe réinitialisé avec succès.");
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


