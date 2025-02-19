package controllers;

import entities.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import services.ServiceUser;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

public class UserController {
    @FXML
    private ListView<User> listViewUsers;
    @FXML
    private TextField txtRecherche;

    private final ServiceUser serviceUser = new ServiceUser();
    private ObservableList<User> userList;

    @FXML
    public void initialize() {
        loadUsers();
        txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> filtrerUtilisateurs(newValue));

        listViewUsers.setCellFactory(lv -> new ListCell<User>() {
            @Override
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox cellLayout = new HBox(20);
                    Text userInfo = new Text(user.getIdUser() + " - " + user.getNomUser() + " " + user.getPrenomUser() + " (" + user.getEmailUser() + ") - " + user.getRole());
                    Button btnDelete = new Button("Supprimer");
                    btnDelete.setStyle("-fx-background-color: #a90d06; -fx-text-fill: white;");
                    btnDelete.setOnAction(e -> supprimerUtilisateur(user));

                    cellLayout.getChildren().addAll(userInfo, btnDelete);
                    setGraphic(cellLayout);
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

    private void supprimerUtilisateur(User user) {
        if (user != null) {
            serviceUser.delete(user.getIdUser());
            loadUsers();
            showAlert("Suppression", "Utilisateur supprimé avec succès.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Charger la page de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Récupérer la fenêtre actuelle et changer la scène
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
