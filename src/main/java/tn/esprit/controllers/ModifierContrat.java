package tn.esprit.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import tn.esprit.models.Contrat;
import tn.esprit.models.TypeContrat;
import tn.esprit.services.ServiceContrat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ModifierContrat {

    @FXML
    private TextField idContratField;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat;
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

        comboTypeContrat.getItems().setAll(TypeContrat.values());
        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);
    }



    public void setContrat(Contrat contrat) {
        this.contratToModify = contrat;

        idContratField.setText(String.valueOf(contrat.getIdContrat()));
        // Convertir le typeContrat en String et le récupérer comme valeur de l'enum dans le ComboBox
        String typeContratString = contrat.getTypeContrat().toString();
        TypeContrat selectedType = TypeContrat.valueOf(typeContratString);
        comboTypeContrat.setValue(selectedType);
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

            TypeContrat typeContrat = comboTypeContrat.getValue();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusContrat = statusActif.isSelected() ? "Actif" : "Inactif";
            int montant = Integer.parseInt(montantField.getText());
            String nomClient = nomClientField.getText();
            String emailClient = emailClientField.getText();

            if (typeContrat == null  && nomClient.isEmpty() && emailClient.isEmpty() &&
                    dateDebut == null && dateFin == null) {
                showAlert("Erreur", "Veuillez remplir les champs svp.");
                return;
            }

            if (typeContrat == null) {
                showAlert("Erreur", "Le type du contrat est obligatoire.");
                return;
            }

            if (nomClient.isEmpty()) {
                showAlert("Erreur", "Le nom du client est obligatoire.");
                return;
            }

            if (emailClient.isEmpty()) {
                showAlert("Erreur", "L'email du client est obligatoire.");
                return;
            }

            if (!isValidEmail(emailClient)) {
                showAlert("Erreur", "L'email est au format incorrect.");
                return;
            }

            if (dateDebut == null || dateFin == null) {
                showAlert("Erreur", " Les dates doivent être sélectionnées !");
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                showAlert("Erreur" , "La date de fin ne peut pas être avant la date de début !");
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



    private boolean isValidEmail(String email) {

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
