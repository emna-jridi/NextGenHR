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

public class ResultTestEmp  implements Initializable {
    @FXML
    private TableView<TestResult> resultsTableView;
    @FXML private TableColumn<TestResult, String> testCategoryCol;
    @FXML private TableColumn<TestResult, Integer> totalQuestionsCol;
    @FXML private TableColumn<TestResult, Integer> correctAnswersCol;
    @FXML private TableColumn<TestResult, Double> scoreCol;
    @FXML private TableColumn<TestResult, LocalDateTime> testDateCol;

    private TestService testService = new TestService();
    private int currentEmployeeId = 12;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        testCategoryCol.setCellValueFactory(new PropertyValueFactory<>("testCategory"));
        totalQuestionsCol.setCellValueFactory(new PropertyValueFactory<>("totalQuestions"));
        correctAnswersCol.setCellValueFactory(new PropertyValueFactory<>("correctAnswers"));
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        testDateCol.setCellValueFactory(new PropertyValueFactory<>("testDate"));
        loadResults();
    }
    public void setCurrentEmployeeId(int employeeId) {
        this.currentEmployeeId = 12;
        loadResults();
    }
    private void loadResults() {
        ObservableList<TestResult> results = FXCollections.observableArrayList(
                testService.getTestResultsByEmployeeId(currentEmployeeId)
        );
        resultsTableView.setItems(results);
    }

    @FXML
    private void handleRefresh() {
        loadResults();
    }
}
