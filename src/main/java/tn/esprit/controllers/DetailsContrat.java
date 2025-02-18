package tn.esprit.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceContrat;

import java.util.List;

public class DetailsContrat {

    @FXML
    private Label labelTypeContrat;

    @FXML
    private Label labelDateDebut;

    @FXML
    private Label labelDateFin;

    @FXML
    private Label labelMontant;

    @FXML
    private Label labelStatut;

    @FXML
    private Label labelNomClient;

    @FXML
    private Label labelEmailClient;

    @FXML
    private ListView<Service> listViewServices; // Afficher les services associés

    private ServiceContrat serviceContrat = new ServiceContrat();
    private Contrat contrat; // Le contrat dont on affiche les détails

    // Méthode pour initialiser et afficher les détails du contrat
    @FXML
    public void initialize() {
        // Supposons que l'ID du contrat soit passé à cette scène via une autre méthode.
        // Ici, on simule l'appel à la méthode getContratById pour obtenir les détails d'un contrat.
        contrat = serviceContrat.getById(1); // Exemple: chercher le contrat avec ID 1

        if (contrat != null) {
            afficherDetailsContrat(contrat);
        }
    }

    // Méthode pour afficher les détails du contrat dans l'interface
    private void afficherDetailsContrat(Contrat contrat) {
        labelTypeContrat.setText(contrat.getTypeContrat());
        labelDateDebut.setText(contrat.getDateDebutContrat().toString());
        labelDateFin.setText(contrat.getDateFinContrat().toString());
        labelMontant.setText(String.valueOf(contrat.getMontantContrat()));
        labelStatut.setText(contrat.getStatusContrat());
        labelNomClient.setText(contrat.getNomClient());
        labelEmailClient.setText(contrat.getEmailClient());

        // Ajouter les services associés à la ListView
        List<Service> services = contrat.getServices();
        listViewServices.getItems().setAll(services);
    }

    // Méthode pour retourner à la liste des contrats
    @FXML
    void retour(ActionEvent event) {
        // Logique pour fermer cette fenêtre et retourner à la liste des contrats.
        // Vous pouvez fermer la scène actuelle ou charger une autre scène contenant la liste des contrats.
        System.out.println("Retour à la liste des contrats");
    }
}
