package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.services.ServiceContrat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ModifierContrat {

    @FXML
    private TextField idContratField;
    @FXML
    private TextField typeContratField;
    @FXML
    private DatePicker dateDebutField;
    @FXML
    private DatePicker dateFinField;
    @FXML
    private RadioButton statusActif;
    @FXML
    private RadioButton statusInactif;
    @FXML
    private TextField montantField;
    @FXML
    private TextField nomClientField;
    @FXML
    private TextField emailClientField;

    private final ServiceContrat contratService;
    private Contrat contratToModify;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ToggleGroup statusGroup;
    public ModifierContrat() {
        contratService = new ServiceContrat();
    }

    @FXML
    public void initialize() {

        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);
    }



    public void setContrat(Contrat contrat) {
        this.contratToModify = contrat;


        idContratField.setText(String.valueOf(contrat.getIdContrat()));
        typeContratField.setText(contrat.getTypeContrat());
        dateDebutField.setValue(contrat.getDateDebutContrat());
        dateFinField.setValue(contrat.getDateFinContrat());
        montantField.setText(String.valueOf(contrat.getMontantContrat()));
        nomClientField.setText(contrat.getNomClient());
        emailClientField.setText(contrat.getEmailClient());


        if ("Actif".equals(contrat.getStatusContrat())) {
            statusActif.setSelected(true);
        } else {
            statusInactif.setSelected(true);
        }

    }


    @FXML
    private void handleSave() {
        try {

            String typeContrat = typeContratField.getText();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusContrat = statusActif.isSelected() ? "Actif" : "Inactif";
            int montant = Integer.parseInt(montantField.getText());
            String nomClient = nomClientField.getText();
            String emailClient = emailClientField.getText();


            if (typeContrat.isEmpty() || nomClient.isEmpty() || emailClient.isEmpty()) {
                System.out.println("Erreur : Tous les champs doivent être remplis !");
                return;
            }

            if (dateDebut == null || dateFin == null) {
                System.out.println("Erreur : Les dates doivent être sélectionnées !");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                System.out.println("Erreur : La date de fin ne peut pas être avant la date de début !");
                return;
            }


            contratToModify.setTypeContrat(typeContrat);
            contratToModify.setDateDebutContrat(dateDebut);
            contratToModify.setDateFinContrat(dateFin);
            contratToModify.setStatusContrat(statusContrat);
            contratToModify.setMontantContrat(montant);
            contratToModify.setNomClient(nomClient);
            contratToModify.setEmailClient(emailClient);


            contratService.update(contratToModify);
            System.out.println("Contrat mis à jour avec succès !");


            ((Stage) montantField.getScene().getWindow()).close();

        } catch (DateTimeParseException e) {
            System.out.println("Erreur : Format de date invalide. Utiliser yyyy-MM-dd !");
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Le montant doit être un nombre valide !");
        }
    }
}
