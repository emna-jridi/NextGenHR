package controllers;

import entities.User;
import entities.User.Role;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.ServiceUser;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    @FXML
    private ListView<User> listViewUsers; // Liste des utilisateurs (ListView)
    @FXML
    private TextField txtRecherche;
    @FXML
    private Button btnSupprimer; // Bouton de suppression

    private final ServiceUser serviceUser = new ServiceUser();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        loadUsers();

        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> filtrerUtilisateurs(newValue));

        // Utilisation d'un CellFactory pour personnaliser l'affichage des utilisateurs dans la ListView
        listViewUsers.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                } else {
                    setText(user.getNomUser() + " " + user.getPrenomUser() + " (" + user.getEmailUser() + ")");
                }
            }
        });
    }

    private void loadUsers() {
        List<User> users = serviceUser.getAll();
        userList = FXCollections.observableArrayList(users);
        listViewUsers.setItems(userList);
    }

    private void filtrerUtilisateurs(String recherche) {
        List<User> filteredList = userList.stream()
                .filter(user -> user.getNomUser().toLowerCase().contains(recherche.toLowerCase()) ||
                        user.getPrenomUser().toLowerCase().contains(recherche.toLowerCase()) ||
                        user.getEmailUser().toLowerCase().contains(recherche.toLowerCase()))
                .collect(Collectors.toList());
        listViewUsers.setItems(FXCollections.observableArrayList(filteredList));
    }

    @FXML
    private void supprimerUtilisateur(ActionEvent event) {
        User selectedUser = listViewUsers.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Supprimer l'utilisateur de la base de données
            serviceUser.delete(selectedUser.getIdUser());

            // Mettre à jour la liste après la suppression
            loadUsers();

            showAlert("Suppression", "Utilisateur supprimé avec succès.");
        } else {
            showAlert("Sélection requise", "Veuillez sélectionner un utilisateur à supprimer.");
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
