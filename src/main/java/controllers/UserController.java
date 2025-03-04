package controllers;

import entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import services.ServiceUser;

public class UserController {

    @FXML
    private ListView<User> listViewUsers;
    @FXML
    private TextField txtRecherche;
    @FXML
    private Button btnRefresh;
    @FXML
    private Label lblUsers;
    @FXML
    private Label lblRHManagers;
    @FXML
    private Label lblEmployees;
    @FXML
    private LineChart<String, Number> userEvolutionChart;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    private final ServiceUser serviceUser = new ServiceUser();
    private ObservableList<User> userList;

    public UserController() {
    }

    @FXML
    public void initialize() {
        this.loadUsers();
        this.txtRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            this.filtrerUtilisateurs(newValue);
        });
        this.listViewUsers.setCellFactory((lv) -> new ListCell<>() {
            protected void updateItem(User user, boolean empty) {
                super.updateItem(user, empty);
                if (!empty && user != null) {
                    HBox cellLayout = new HBox(20.0);
                    cellLayout.setAlignment(Pos.CENTER_LEFT);
                    cellLayout.setPrefWidth(listViewUsers.getWidth() - 30);

                    Text userInfo = new Text(user.getIdUser() + " - " + user.getNomUser() + " " +
                            user.getPrenomUser() + " (" + user.getEmailUser() + ") - " +
                            user.getRole().name());

                    Button btnToggleStatus = new Button(user.isActive() ? "Désactiver" : "Activer");
                    btnToggleStatus.setStyle("-fx-background-color: " + (user.isActive() ? "#FFC20E" : "#4CAF50") + "; -fx-text-fill: black;");
                    btnToggleStatus.setOnAction((e) -> {
                        UserController.this.toggleUserStatus(user, btnToggleStatus);
                    });

                    Button btnDelete = new Button("Supprimer");
                    btnDelete.setStyle("-fx-background-color: #a90d06; -fx-text-fill: white;");
                    btnDelete.setOnAction((e) -> {
                        UserController.this.confirmerSuppression(user);
                    });

                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    cellLayout.getChildren().addAll(userInfo, spacer, btnToggleStatus, btnDelete);
                    this.setGraphic(cellLayout);
                } else {
                    this.setText(null);
                    this.setGraphic(null);
                }
            }
        });
    }


    private void filtrerUtilisateurs(String recherche) {
        List<User> filteredList = (List)this.userList.stream().filter((user) -> {
            return user.getNomUser().toLowerCase().contains(recherche.toLowerCase()) || user.getPrenomUser().toLowerCase().contains(recherche.toLowerCase()) || user.getEmailUser().toLowerCase().contains(recherche.toLowerCase());
        }).collect(Collectors.toList());
        this.listViewUsers.setItems(FXCollections.observableArrayList(filteredList));
    }

    private void toggleUserStatus(User user, Button button) {
        // Affichage de la confirmation avant de changer le statut
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirmer l'activation/désactivation");
        String action = user.isActive() ? "désactiver" : "activer";
        alert.setContentText("Êtes-vous sûr de vouloir " + action + " le compte de " + user.getNomUser() + " ?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Si l'utilisateur confirme, on change le statut
            boolean newStatus = !user.isActive();
            this.serviceUser.toggleUserStatus(user.getIdUser(), newStatus);
            user.setActive(newStatus);
            button.setText(newStatus ? "Désactiver" : "Activer");
            button.setStyle("-fx-background-color: " + (newStatus ? "#FFC20E" : "#4CAF50") + "; -fx-text-fill: black;");
            this.listViewUsers.refresh();
        } else {
            // Si l'utilisateur annule, on ne fait rien
            System.out.println("Action annulée !");
        }
    }

    private void confirmerSuppression(User user) {
        if (user != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Supprimer l'utilisateur ?");
            String var10001 = user.getNomUser();
            alert.setContentText("Êtes-vous sûr de vouloir supprimer " + var10001 + " " + user.getPrenomUser() + " ?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                this.demanderValidationFinale(user);
            }

        }
    }

    private void demanderValidationFinale(User user) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Validation Finale");
        alert.setHeaderText("Dernière confirmation !");
        alert.setContentText("Cette action est définitive. Confirmez-vous la suppression de " + user.getNomUser() + " ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            this.supprimerUtilisateur(user);
        }

    }

    private void supprimerUtilisateur(User user) {
        this.serviceUser.delete(user.getIdUser());
        this.loadUsers();
        this.showAlert("Suppression réussie", "L'utilisateur a été supprimé avec succès.");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((String)null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void trierUtilisateursParRole() {
        List<User> sortedUsers = new ArrayList<>(this.listViewUsers.getItems());
        sortedUsers.sort(Comparator.comparing(User::getRole));
        this.listViewUsers.getItems().setAll(sortedUsers);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/Login.fxml"));
            Scene loginScene = new Scene((Parent)loader.load());
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(loginScene);
            stage.show();
        } catch (IOException var5) {
            IOException e = var5;
            e.printStackTrace();
        }
    }


    private void loadUsers() {
        List<User> users = this.serviceUser.getAll();
        this.userList = FXCollections.observableArrayList(users);
        this.listViewUsers.setItems(this.userList);
        updateUserStats();
    }
    private void updateUserStatsChart(long totalUsers, long totalRHManagers, long totalEmployees) {
        userEvolutionChart.getData().clear(); // On vide les anciennes données

        XYChart.Series<String, Number> rhSeries = new XYChart.Series<>();
        rhSeries.setName("Responsables RH");

        XYChart.Series<String, Number> empSeries = new XYChart.Series<>();
        empSeries.setName("Employés");

        // Utilisation de catégories distinctes pour éviter le chevauchement
        rhSeries.getData().add(new XYChart.Data<>("RH Managers", totalRHManagers));
        empSeries.getData().add(new XYChart.Data<>("Employés", totalEmployees));

        userEvolutionChart.getData().addAll(rhSeries, empSeries);
    }



    private void updateUserStats() {
        long totalUsers = userList.size();
        long totalRHManagers = userList.stream()
                .filter(user -> user.getRole().name().equals("RESPONSABLE_RH"))
                .count();
        long totalEmployees = userList.stream().filter(user -> user.getRole().toString().equalsIgnoreCase("Employe")).count();

        lblUsers.setText(String.valueOf(totalUsers));
        lblRHManagers.setText(String.valueOf(totalRHManagers));
        lblEmployees.setText(String.valueOf(totalEmployees));
        updateUserStatsChart(totalUsers, totalRHManagers, totalEmployees);
    }

    @FXML
    private void handleRefresh() {
        this.loadUsers();
        System.out.println("\ud83d\udd04 Liste des utilisateurs rafraîchie !");
    }
}
