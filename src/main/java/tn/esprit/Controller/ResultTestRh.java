package tn.esprit.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.TestResult;
import tn.esprit.services.TestService;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class ResultTestRh  implements Initializable {
    @FXML
    private TableView<TestResult> resultsTableView;
    @FXML private TableColumn<TestResult, String> employeeNameCol;
    @FXML private TableColumn<TestResult, String> testCategoryCol;
    @FXML private TableColumn<TestResult, Integer> totalQuestionsCol;
    @FXML private TableColumn<TestResult, Integer> correctAnswersCol;
    @FXML private TableColumn<TestResult, Double> scoreCol;
    @FXML private TableColumn<TestResult, LocalDateTime> testDateCol;

    private TestService testService = new TestService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeNameCol.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        testCategoryCol.setCellValueFactory(new PropertyValueFactory<>("testCategory"));
        totalQuestionsCol.setCellValueFactory(new PropertyValueFactory<>("totalQuestions"));
        correctAnswersCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswers"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        testDateCol.setCellValueFactory(new PropertyValueFactory<>("testDate"));
        loadResults();
    }

    // Méthode pour charger les résultats
    private void loadResults() {
        ObservableList<TestResult> results = FXCollections.observableArrayList(testService.getAllTestResults());
        resultsTableView.setItems(results);
    }
    @FXML
    private void handleRefresh() {
        loadResults();
    }
}
