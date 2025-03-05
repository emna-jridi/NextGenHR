package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import tn.esprit.models.Contrat;
import tn.esprit.models.ModePaiement;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceContrat;
import tn.esprit.services.ServiceService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ModifierContrat {

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
    @FXML
    private TextField numTelClientField;
    @FXML
    private Label emailValidationLabel;
    @FXML
    private Label numTelValidationLabel;
    @FXML
    private Label montantValidationLabel;
    @FXML
    private CheckComboBox<String> checkComboBoxServices;
    @FXML
    private ComboBox<ModePaiement> comboBoxModePaiement;

    private Contrat contratToModify;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ToggleGroup statusGroup;
    private ListContrats listContratsController;

    private ServiceContrat contratService = new ServiceContrat();
    private ServiceService serviceService = new ServiceService();



    @FXML
    public void initialize() {

        statusGroup = new ToggleGroup();
        statusActif.setToggleGroup(statusGroup);
        statusInactif.setToggleGroup(statusGroup);

        loadServices();
        loadModePaiement();


        // Validation de l'email
        emailClientField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                emailValidationLabel.setText("");
            } else if (isValidEmail(newValue)) {
                emailValidationLabel.setText("Email valide");
                emailValidationLabel.setStyle("-fx-text-fill: #71e071;");
            } else {
                emailValidationLabel.setText("Email invalide");
                emailValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            }
        });


        // Validation du numéro de téléphone
        numTelClientField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                numTelValidationLabel.setText("");
            } else if (!newValue.matches("\\d*")) {
                numTelValidationLabel.setText("Le numéro doit contenir des chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (newValue.length() < 8) {
                numTelValidationLabel.setText("Le numéro doit contenir 8 chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (newValue.length() == 8) {
                numTelValidationLabel.setText("Numéro valide");
                numTelValidationLabel.setStyle("-fx-text-fill: #71e071;");
            } else {
                numTelValidationLabel.setText("Le numéro ne doit pas dépasser 8 chiffres.");
                numTelValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            }
        });


        // Validation du montant
        montantField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                montantValidationLabel.setText("");
            } else if (!newValue.matches("\\d+")) {
                montantValidationLabel.setText("Le montant doit contenir des chiffres.");
                montantValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else if (Integer.parseInt(newValue) <= 0) {
                montantValidationLabel.setText("Le montant doit être > à 0.");
                montantValidationLabel.setStyle("-fx-text-fill: #dc5b5b;");
            } else {
                montantValidationLabel.setText("Montant valide");
                montantValidationLabel.setStyle("-fx-text-fill: #71e071;");
            }
        });



    }

    private void loadServices() {
        // Récupérer tous les services
        List<Service> services = serviceService.getAll();
        // Extraire uniquement les noms des services dans une liste ObservableList
        ObservableList<String> serviceNames = FXCollections.observableArrayList();
        for (Service service : services) {
            serviceNames.add(service.getNomService());
        }
        // Ajouter les noms des services au CheckComboBox
        checkComboBoxServices.getItems().setAll(serviceNames);
    }


    private void loadModePaiement() {
        ObservableList<ModePaiement> modePaiements = FXCollections.observableArrayList(ModePaiement.values());
        comboBoxModePaiement.setItems(modePaiements);
    }





    @FXML
    private void handleCancel() {
        listContratsController.showAjouterContratForm();
        ((Stage) nomClientField.getScene().getWindow()).close();
    }




    public void setListContratsController(ListContrats controller) {
        this.listContratsController = controller;
    }




    //initialiser les champs avec données du contrat
    public void setContrat(Contrat contrat, ListContrats listContratsController) {
        this.contratToModify = contrat;
        this.listContratsController = listContratsController;

        dateDebutField.setValue(contrat.getDateDebutContrat());
        dateFinField.setValue(contrat.getDateFinContrat());
        montantField.setText(String.valueOf(contrat.getMontantContrat()));
        nomClientField.setText(contrat.getNomClient());
        emailClientField.setText(contrat.getEmailClient());
        numTelClientField.setText(contrat.getTelephoneClient());

        if ("Actif".equals(contrat.getStatusContrat())) {
            statusActif.setSelected(true);
        } else {
            statusInactif.setSelected(true);
        }



     // Charger les services associés à ce contrat
        List<Service> services = contratService.getServicesByContratId(contrat.getIdContrat());
        ObservableList<String> selectedServices = FXCollections.observableArrayList();

        // Ajouter les services liés au contrat dans une liste observable
        for (Service service : services) {
            selectedServices.add(service.getNomService());
        }

        // Décocher toutes les cases avant de les cocher de nouveau
        checkComboBoxServices.getCheckModel().clearChecks();

        // Cocher les services associés au contrat
        for (int i = 0; i < checkComboBoxServices.getItems().size(); i++) {
            if (selectedServices.contains(checkComboBoxServices.getItems().get(i))) {
                checkComboBoxServices.getCheckModel().check(i);
            }
        }




        // Sélectionner le mode de paiement actuel dans le ComboBox
        ModePaiement modePaiement = contrat.getModeDePaiement();
        if (modePaiement != null) {
            comboBoxModePaiement.setValue(modePaiement);
        }



    }




    //enregistrer les modifications
    @FXML
    private void handleSave() {
        try {
            LocalDate dateDebut = dateDebutField.getValue();
            LocalDate dateFin = dateFinField.getValue();
            String statusContrat = statusActif.isSelected() ? "Actif" : "Inactif";
            int montant = Integer.parseInt(montantField.getText());
            String nomClient = nomClientField.getText();
            String emailClient = emailClientField.getText();
            String numTelClient = numTelClientField.getText();


            if (nomClient.isEmpty() && emailClient.isEmpty() && numTelClient.isEmpty() &&
                    dateDebut == null && dateFin == null) {
                showAlert("Erreur", "Veuillez remplir les champs svp.");
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

            if (numTelClient.isEmpty()) {
                showAlert("Erreur", "Le numéro de téléphone de client est obligatoire.");
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

            // Récupérer le mode de paiement sélectionné dans le ComboBox
            ModePaiement modePaiement = comboBoxModePaiement.getValue();
            if (modePaiement == null) {
                showAlert("Erreur", "Le mode de paiement est obligatoire.");
                return;
            }


            // Mise à jour des données
            contratToModify.setDateDebutContrat(dateDebut);
            contratToModify.setDateFinContrat(dateFin);
            contratToModify.setStatusContrat(statusContrat);
            contratToModify.setMontantContrat(montant);
            contratToModify.setNomClient(nomClient);
            contratToModify.setEmailClient(emailClient);
            contratToModify.setTelephoneClient(numTelClient);
            contratToModify.setModeDePaiement(modePaiement);


                // Récupérer les services sélectionnés dans le CheckComboBox
                List<Service> selectedServices = new ArrayList<>();
                for (String serviceName : checkComboBoxServices.getCheckModel().getCheckedItems()) {
                    int serviceId = getServiceIdByName(serviceName);
                    if (serviceId != -1) {
                        Service service = serviceService.getById(serviceId);
                        selectedServices.add(service);
                    }
                }

          // Mettre à jour le contrat avec les nouveaux services
            contratToModify.setServices(selectedServices);

            contratService.update(contratToModify);
            showAlert("Succès", "Le contrat a été mise à jour avec succès.");

            // Appeler la méthode dans ListContrats pour afficher le formulaire AjouterContrat
            listContratsController.showAjouterContratForm();

            // Fermer seulement le formulaire actuel ModifierContrat
            ((Stage) nomClientField.getScene().getWindow()).close();

        } catch (DateTimeParseException e) {
            System.out.println("Erreur : Format de date invalide. Utiliser yyyy-MM-dd !");
        } catch (NumberFormatException e) {
            System.out.println("Erreur : Le montant doit être un nombre valide !");
        }
    }





    private int getServiceIdByName(String serviceName) {
        // Appeler la méthode getServicesByName dans ServiceService
        List<Service> services = serviceService.getServicesByName(serviceName);
        if (!services.isEmpty()) {
            // Retourner l'ID du premier service trouvé
            return services.get(0).getIdService();
        }
        return -1;
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
