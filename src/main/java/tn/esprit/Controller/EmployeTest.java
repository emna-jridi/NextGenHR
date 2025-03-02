package tn.esprit.Controller;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import tn.esprit.models.Questions;
import tn.esprit.models.TestAssignment;
import tn.esprit.models.TestResult;
import tn.esprit.services.QuizApiService;
import tn.esprit.services.TestService;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
public class EmployeTest implements Initializable {

    @FXML private Label welcomeLabel;
    @FXML private TableView<TestAssignment> assignedTestsTable;
    @FXML private VBox testBox;
    @FXML private Label questionLabel;
    @FXML private VBox answersBox;
    @FXML private Button nextButton;
    @FXML private Label resultLabel;

    // Initialize services directly
    private TestService dbManager = new TestService();
    private QuizApiService quizAPIService;
    private int employeeId = 12;//test
    private String employeeName = "Test Employee";

    private List<Questions> currentQuestions;
    private int currentQuestionIndex;
    private int correctAnswers;
    private TestAssignment currentAssignment;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setupAssignedTestsTable();

        testBox.setVisible(false);
        quizAPIService = new QuizApiService();
        welcomeLabel.setText("Bienvenue, " + employeeName );

        // Load assigned tests immediately after initialization
        loadAssignedTests();
    }

    public void setServices(TestService dbManager, QuizApiService quizAPIService,
                            int employeeId, String employeeName) {
        if (dbManager != null) {
            this.dbManager = dbManager;
        }

        if (quizAPIService != null) {
            this.quizAPIService = quizAPIService;
        } else if (this.quizAPIService == null) {
            this.quizAPIService = new QuizApiService();
        }

        this.employeeId = employeeId;
        this.employeeName = employeeName != null ? employeeName : "";

        welcomeLabel.setText("Bienvenue, " + this.employeeName +  ")!");
        loadAssignedTests();
    }

    private void setupAssignedTestsTable() {
        assignedTestsTable.getColumns().clear();
        TableColumn<TestAssignment, String> categoryCol = new TableColumn<>("Catégorie");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("testCategory"));
        TableColumn<TestAssignment, String> deadlineCol = new TableColumn<>("Échéance");
        deadlineCol.setCellValueFactory(cellData -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            return new javafx.beans.property.SimpleStringProperty(
                    cellData.getValue().getDeadline().format(formatter));
        });

        TableColumn<TestAssignment, String> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<TestAssignment, String>() {
            final Button startButton = new Button("Commencer");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    TestAssignment assignment = getTableView().getItems().get(getIndex());
                    startButton.setOnAction(event -> startTest(assignment));
                    setGraphic(startButton);
                    setText(null);
                }
            }
        });

        assignedTestsTable.getColumns().addAll( categoryCol, deadlineCol, actionCol);
    }

    private void loadAssignedTests() {
        try {
            // Ensure dbManager is initialized
            if (dbManager == null) {
                dbManager = new TestService();
            }

            List<TestAssignment> assignments = dbManager.getAssignedTests(employeeId);
            assignedTestsTable.setItems(FXCollections.observableArrayList(assignments));
            System.out.println("Loading assignments for employee ID: " + employeeId);
            System.out.println("Found " + assignments.size() + " assignments");
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les tests assignés: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void startTest(TestAssignment assignment) {
        try {
            // Ensure quizAPIService is initialized
            if (quizAPIService == null) {
                quizAPIService = new QuizApiService();
            }

            currentAssignment = assignment;

            // Charger les questions depuis l'API
            currentQuestions = quizAPIService.fetchQuestions(
                    assignment.getTestCategory(),
                    assignment.getQuestionsCount()
            );

            currentQuestionIndex = 0;
            correctAnswers = 0;
            displayQuestion();
            assignedTestsTable.setVisible(false);
            testBox.setVisible(true);

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des questions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void displayQuestion() {
        Questions currentQuestion = currentQuestions.get(currentQuestionIndex);
        questionLabel.setText((currentQuestionIndex + 1) + "/" + currentQuestions.size() +
                ": " + currentQuestion.getQuestion());

        answersBox.getChildren().clear();
        ToggleGroup answersGroup = new ToggleGroup();
        for (String answer : currentQuestion.getAnswers()) {
            RadioButton rb = new RadioButton(answer);
            rb.setToggleGroup(answersGroup);
            rb.setWrapText(true);
            answersBox.getChildren().add(rb);
        }
        nextButton.setDisable(true);
        answersGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            nextButton.setDisable(newVal == null);
        });

        nextButton.setOnAction(e -> {
            // Vérifier la réponse
            RadioButton selectedRB = (RadioButton) answersGroup.getSelectedToggle();
            if (selectedRB != null) {
                int selectedIndex = answersBox.getChildren().indexOf(selectedRB);
                String selectedAnswer = "answer_" + (char)('a' + selectedIndex);

                if (selectedAnswer.equals(currentQuestion.getCorrectAnswer())) {
                    correctAnswers++;
                }
            }
            currentQuestionIndex++;
            if (currentQuestionIndex < currentQuestions.size()) {
                displayQuestion();
            } else {
                finishTest();
            }
        });
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Erreur") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void finishTest() {
        try {
            if (dbManager == null) {
                dbManager = new TestService();
            }
            int totalQuestions = currentQuestions.size();
            double score = (correctAnswers / (double) totalQuestions) * 100;
            TestResult testResult = new TestResult();
            testResult.setEmployeeId(employeeId);
            testResult.setTestCategory(currentAssignment.getTestCategory());
            testResult.setTotalQuestions(totalQuestions);
            testResult.setCorrectAnswers(correctAnswers);
            testResult.setScore(score);
            testResult.setAnalysisNotes("Test terminé avec succès");
            testResult.setTestDate(LocalDateTime.now());

            int resultId = dbManager.saveTestResult(testResult);

            if (resultId != -1) {
                dbManager.updateTestAssignmentStatus(currentAssignment.getId(), "COMPLETED");

                resultLabel.setText("Test terminé!\nScore: " + score + "%");
                resultLabel.setVisible(true);
                showAlert("Test terminé", "Votre score est de " + score + "%.");
            } else {
                showAlert("Erreur", "Impossible d'enregistrer le résultat.");
            }

            testBox.setVisible(false);
            assignedTestsTable.setVisible(true);
            loadAssignedTests(); // Rafraîchir la liste des tests assignés

        } catch (Exception e) {
            showAlert("Erreur", "Problème lors de la finalisation du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}