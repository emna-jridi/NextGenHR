package controllers;

import entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import services.ServiceUser;

public class ProfileController {

    @FXML
    private TextField txtNom;

    @FXML
    private TextField txtPrenom;

    @FXML
    private DatePicker dateNaissance;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtTelephone;

    @FXML
    private PasswordField txtMotDePasse;
// 🔥 Utilisation d'un PasswordField au lieu d'un TextField

    @FXML
    private Button btnMettreAJour;

    private ServiceUser serviceUser;
    private User utilisateurConnecte; // L'utilisateur actuellement connecté

    public ProfileController() {
        this.serviceUser = new ServiceUser();
    }

    @FXML
    private void initialize() {
        // 🔥 Charger les vraies données de l'utilisateur connecté
        chargerDonneesUtilisateur();
    }

    private void chargerDonneesUtilisateur() {
        int userId = 1; // 🔥 Remplace par l'ID de l'utilisateur connecté (peut être récupéré d'une session)
        utilisateurConnecte = serviceUser.getById(userId);

        if (utilisateurConnecte != null) {
            txtNom.setText(utilisateurConnecte.getNomUser());
            txtPrenom.setText(utilisateurConnecte.getPrenomUser());
            dateNaissance.setValue(utilisateurConnecte.getDateNaissanceUser());
            txtEmail.setText(utilisateurConnecte.getEmailUser());
            txtTelephone.setText(utilisateurConnecte.getTelephoneUser());
            txtMotDePasse.setText(utilisateurConnecte.getPassword()); // ⚠️ Idéalement, ne pas afficher en clair
        } else {
            System.out.println("❌ Erreur : Impossible de récupérer l'utilisateur !");
        }
    }

    @FXML
    private void mettreAJourUtilisateur() {
        if (utilisateurConnecte != null) {
            utilisateurConnecte.setNomUser(txtNom.getText());
            utilisateurConnecte.setPrenomUser(txtPrenom.getText());
            utilisateurConnecte.setDateNaissanceUser(dateNaissance.getValue());
            utilisateurConnecte.setEmailUser(txtEmail.getText());
            utilisateurConnecte.setTelephoneUser(txtTelephone.getText());
            utilisateurConnecte.setPassword(txtMotDePasse.getText()); // 🔥 Stocker le mot de passe hashé idéalement

            // 🔥 Mettre à jour dans la base de données
            serviceUser.update(utilisateurConnecte);

            // 🔔 Affichage de la confirmation
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise à Jour");
            alert.setHeaderText(null);
            alert.setContentText("Les informations ont été mises à jour avec succès !");
            alert.showAndWait();
        } else {
            System.out.println("❌ Erreur : Aucune donnée utilisateur trouvée !");
        }
    }
}
