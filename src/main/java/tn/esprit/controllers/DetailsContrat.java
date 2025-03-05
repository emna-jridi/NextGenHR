package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import tn.esprit.models.Contrat;
import tn.esprit.models.Service;

public class DetailsContrat {

    @FXML private Label lblNomClient;
    @FXML private Label lblEmailClient;
    @FXML private Label lblNumTelClient;
    @FXML private Label lblDateDebut;
    @FXML private Label lblDateFin;
    @FXML private Label lblMontant;
    @FXML private Label lblStatut;
    @FXML private Label lblServices;
    @FXML private Label lblModePaie;




    public void setContrat(Contrat contrat) {
        lblNomClient.setText(contrat.getNomClient());
        lblEmailClient.setText(contrat.getEmailClient());
        lblNumTelClient.setText(contrat.getTelephoneClient());
        lblDateDebut.setText("" + contrat.getDateDebutContrat());
        lblDateFin.setText("" + contrat.getDateFinContrat());
        lblMontant.setText(contrat.getMontantContrat() + " DT");
        lblStatut.setText(contrat.getStatusContrat());
        lblModePaie.setText(contrat.getModeDePaiement().name());

        // Construire une chaîne formatée pour afficher chaque service sur une ligne
        StringBuilder servicesText = new StringBuilder("");
        for (Service service : contrat.getServices()) {
            servicesText.append("- ").append(service.getNomService())
                    .append(":   ").append(service.getDescriptionService())
                    .append("\n");
        }

// Mettre à jour le label
        lblServices.setText(servicesText.toString());

    }
}
