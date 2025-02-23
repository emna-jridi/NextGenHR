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

public class ModifierContrat {

    @FXML
    private TextField idContratField;
    @FXML
    private ComboBox<TypeContrat> comboTypeContrat; // Référence au ComboBox

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
        // Remplir le ComboBox avec les valeurs de l'enum TypeContrat
        comboTypeContrat.getItems().setAll(TypeContrat.values());
        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);
    }



    public void setContrat(Contrat contrat) {
        this.contratToModify = contrat;


        idContratField.setText(String.valueOf(contrat.getIdContrat()));
        // Convertir le typeContrat en String et le récupérer comme valeur de l'enum dans le ComboBox
        String typeContratString = contrat.getTypeContrat().toString(); // Assurez-vous que getTypeContrat() retourne un TypeContrat
        TypeContrat selectedType = TypeContrat.valueOf(typeContratString); // Convertir String en Enum
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

            // Récupérer la valeur sélectionnée dans le ComboBox pour TypeContrat
            TypeContrat typeContrat = comboTypeContrat.getValue();
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusContrat = statusActif.isSelected() ? "Actif" : "Inactif";
            int montant = Integer.parseInt(montantField.getText());
            String nomClient = nomClientField.getText();
            String emailClient = emailClientField.getText();


            if (typeContrat == null || nomClient.isEmpty() || emailClient.isEmpty()) {
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
