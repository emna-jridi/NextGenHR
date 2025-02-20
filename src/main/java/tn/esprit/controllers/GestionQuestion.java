package tn.esprit.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import tn.esprit.models.Question;
import tn.esprit.services.ServiceQuestion;

import java.net.URL;
import java.util.*;

public class GestionQuestion implements Initializable {
    @FXML private TextField tfQuestion;
    // @FXML private ComboBox<String> typeQuestion;
    @FXML private TextField tfPoints;
    @FXML private TextArea taOptions;
    @FXML private TextField tfCorrectResponse;
    @FXML private TextField tfTestId;

    @FXML private TableView<Question> tvQuestions;
    @FXML private TableColumn<Question, String> colQuestion;
    @FXML private TableColumn<Question, String> colCorrectResponse;
   // @FXML private TableColumn<Question, String> colType;
    @FXML private TableColumn<Question, Integer> colPoints;

    private final ServiceQuestion serviceQuestion = new ServiceQuestion();
    private ObservableList<Question> questionsList;
    private int selectedId = -1;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize TableView
        colQuestion.setCellValueFactory(new PropertyValueFactory<>("texte"));
      //  colType.setCellValueFactory(new PropertyValueFactory<>("type_question"));
        colPoints.setCellValueFactory(new PropertyValueFactory<>("points"));
        colCorrectResponse.setCellValueFactory(new PropertyValueFactory<>("correct_response") );
        System.out.println(colQuestion.getText());
        System.out.println(colCorrectResponse.getText());
      refreshTableView();
    }

    @FXML
    void addQuestion(ActionEvent event) {
        if (tfQuestion.getText().trim().isEmpty() ||
                tfPoints.getText().trim().isEmpty() || tfCorrectResponse.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires!");
            return;
        }
        if (!tfQuestion.getText().matches("[a-zA-Z0-9 ]+") || !tfCorrectResponse.getText().matches("[a-zA-Z0-9 ]+")) {
            showAlert("Erreur", "Le nom et la question et la reponse  doivent contenir  des lettres et des chiffres  .");
            return;
        }

            List<String> options = new ArrayList<>();
            options = Arrays.asList(taOptions.getText().trim().split("\n"));
            try {
                Integer.parseInt(tfPoints.getText().trim());
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le champs 'Points'  doit être entier !");
                return;
            }

            Question q = new Question();
            q.setTexte(tfQuestion.getText());
            // q.setType(typeQuestion.getValue());
            q.setPoints(Integer.parseInt(tfPoints.getText()));
            q.setCorrectResponse(tfCorrectResponse.getText());
            q.setOptions(options);
            q.setTestId(2);
        System.out.println(q);


            serviceQuestion.add(q);
            refreshTableView();
            clearFields();
            showAlert("Succès", "Question ajoutée avec succès!");

    }

    @FXML
    void updateQuestion(ActionEvent event) {
        if (selectedId == -1) {
            showAlert("Erreur", "Veuillez sélectionner une question à modifier!");
            return;
        }
        if (tfQuestion.getText().trim().isEmpty() ||
                tfPoints.getText().trim().isEmpty() ||
               tfCorrectResponse.getText().trim().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires!");
        }
        if (!tfQuestion.getText().matches("[a-zA-Z0-9 ]+") || !tfCorrectResponse.getText().matches("[a-zA-Z0-9 ]+")) {
            showAlert("Erreur", "Le nom et la question et la reponse  doivent contenir  des lettres et des chiffres .");
            return;
        }

        try {
            Integer.parseInt(tfPoints.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le champs 'Points'  doit être entier !");
            return;
        }
            Question q = new Question();
            q.setId(selectedId);
            q.setTexte(tfQuestion.getText());
            // q.setType(typeQuestion.getValue());
            q.setPoints(Integer.parseInt(tfPoints.getText()));
            q.setCorrectResponse(tfCorrectResponse.getText());
           q.setTestId(2);


        if (!taOptions.getText().trim().isEmpty()) {
                List<String> options = Arrays.asList(taOptions.getText().split("\n"));
                q.setOptions(options);
            } else {
                q.setOptions(new ArrayList<>());
            }

            serviceQuestion.update(q);
            refreshTableView();
            clearFields();
            selectedId = -1;
            showAlert("Succès", "Question modifiée avec succès!");

    }

    @FXML
    void deleteQuestion(ActionEvent event) {
        Question selectedQuestion = tvQuestions.getSelectionModel().getSelectedItem();
        if (selectedQuestion == null) {
            showAlert("Erreur", "Veuillez sélectionner une question à supprimer!");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation");
        confirmation.setHeaderText("Supprimer la question");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer cette question ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            serviceQuestion.delete(selectedQuestion); // Passe l'objet au service
           refreshTableView();
            clearFields();
            selectedId = -1;
            showAlert("Succès", "Question supprimée avec succès!");
        }
    }

    @FXML
    void onQuestionSelect(MouseEvent event) {
        Question selected = tvQuestions.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedId = selected.getId();
            tfQuestion.setText(selected.getTexte());
            // typeQuestion.setValue(selected.getType());
            tfPoints.setText(String.valueOf(selected.getPoints()));
           // tfTestId.setText(String.valueOf(selected.getTestId()));
            tfCorrectResponse.setText(selected.getCorrectResponse());

            if (selected.getOptions() != null) {
                taOptions.setText(String.join("\n", selected.getOptions()));
            } else {
                taOptions.clear();
            }
        }
    }
//debug table vide
    private void refreshTableView() {
        if (tvQuestions == null) {
            System.out.println("TableView is NULL!");
            return;
        }
        questionsList = FXCollections.observableArrayList(serviceQuestion.getAll());
        System.out.println("Retrieved questions: " + questionsList); // Debugging

        if (questionsList.isEmpty()) {
            System.out.println("No questions retrieved.");
        }

        tvQuestions.setItems(questionsList);
        System.out.println("TableView updated with " + questionsList.size() + " items.");
    }

    private void clearFields() {
        tfQuestion.clear();
        // typeQuestion.setValue(null);
        tfPoints.clear();
      //  tfTestId.clear();
        tfCorrectResponse.clear();
        taOptions.clear();
        selectedId = -1;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
