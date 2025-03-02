package tn.esprit.Controller;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.esprit.models.TestAssignment;
import tn.esprit.services.TestService;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class RhTest implements Initializable {

    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField employeeIdField;
    @FXML private Spinner<Integer> questionsSpinner;
    @FXML private DatePicker deadlineDatePicker;
    @FXML private TextArea notesTextArea;
    @FXML private Button assignButton;
    @FXML private TableView<TestAssignment> assignmentsTableView;
    @FXML private ChoiceBox<String> employeeChoiceBox;
    private Map<String, Integer> employeeMap = new HashMap<>();
    private TestService dbManager = new TestService();
    private int currentAdminId = 1;

    public void initialize(URL location, ResourceBundle resources) {
        // Initialiser les combobox
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Linux", "DevOps", "Docker", "SQL", "CMS", "Code"
        ));
        SpinnerValueFactory<Integer> valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(5, 20, 10);
        questionsSpinner.setValueFactory(valueFactory);
        setupAssignmentsTable();

        deadlineDatePicker.setValue(LocalDate.now().plusDays(1));
        loadAssignments();
        loadEmployeeNames();
    }
    public void setServices(TestService dbManager, int adminId) {
        if (dbManager != null) {
            this.dbManager = dbManager;
        }
        this.currentAdminId = adminId;
        loadAssignments();
    }

    private void setupAssignmentsTable() {
        assignmentsTableView.getColumns().clear();
        TableColumn<TestAssignment, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nomUser"));

        TableColumn<TestAssignment, String> categoryCol = new TableColumn<>("Catégorie");
        categoryCol.setCellValueFactory(new PropertyValueFactory<>("testCategory"));

        TableColumn<TestAssignment, Integer> questionsCol = new TableColumn<>("Questions");
        questionsCol.setCellValueFactory(new PropertyValueFactory<>("questionsCount"));

        TableColumn<TestAssignment, LocalDateTime> deadlineCol = new TableColumn<>("Échéance");
        deadlineCol.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        TableColumn<TestAssignment, String> statusCol = new TableColumn<>("Statut");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        assignmentsTableView.getColumns().addAll( nomCol, categoryCol,
                questionsCol, deadlineCol, statusCol);
    }

    private void loadAssignments() {
        try {
            if (dbManager == null) {
                // Ensure dbManager is initialized
                dbManager = new TestService();
            }
            ObservableList<TestAssignment> assignments = FXCollections.observableArrayList(
                    dbManager.getAllAssignmentsByHR(currentAdminId)
            );
            assignmentsTableView.setItems(assignments);
            assignmentsTableView.setItems(assignments);
        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger les affectations: " + e.getMessage());
            e.printStackTrace();
        }
    }



    @FXML
    private void handleAssignTest() {
        try {
            // Verify dbManager is not null
            if (dbManager == null) {
                dbManager = new TestService();
            }

            // Valider les entrées
            if (employeeChoiceBox.getValue() == null || categoryComboBox.getValue() == null
                    || deadlineDatePicker.getValue() == null) {
                showAlert("Erreur", "Veuillez remplir tous les champs requis.");
                return;
            }
            String selectedEmployee = employeeChoiceBox.getValue();
            int employeeId = employeeMap.get(selectedEmployee);
            String category = categoryComboBox.getValue();
            int questions = questionsSpinner.getValue();
            LocalDateTime deadline = LocalDateTime.of(
                    deadlineDatePicker.getValue(),
                    LocalTime.of(23, 59, 59)
            );
            if (dbManager.getEmployeeById(employeeId) == null) {
                showAlert("Erreur", "Employé non trouvé avec l'ID: " + employeeId);
                return;
            }
            TestAssignment assignment = new TestAssignment();
            assignment.setEmployeeId(employeeId);
            assignment.setNomUser(selectedEmployee);
            assignment.setTestCategory(category);
            assignment.setAssignedBy(currentAdminId);
            assignment.setQuestionsCount(questions);
            assignment.setDeadline(deadline);
            assignment.setStatus("PENDING");
            assignment.setNotes(notesTextArea.getText());
            dbManager.assignTestToEmployee(assignment);
            loadAssignments();
            employeeChoiceBox.getSelectionModel().clearSelection();
            categoryComboBox.getSelectionModel().clearSelection();
            questionsSpinner.getValueFactory().setValue(10);
            notesTextArea.clear();

            showAlert("Succès", "Test assigné avec succès à l'employé: " + selectedEmployee);

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'assignation du test: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadEmployeeNames() {
        try {
            if (dbManager == null) {
                dbManager = new TestService();
            }
            List<Map.Entry<Integer, String>> employees = dbManager.getAllEmployees();
            employeeMap.clear();
            List<String> employeeNames = new ArrayList<>();

            for (Map.Entry<Integer, String> employee : employees) {
                employeeMap.put(employee.getValue(), employee.getKey());
                employeeNames.add(employee.getValue());
            }

            employeeChoiceBox.setItems(FXCollections.observableArrayList(employeeNames));

        } catch (Exception e) {
            showAlert("Erreur", "Impossible de charger la liste des employés: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(title.equals("Erreur") ? Alert.AlertType.ERROR : Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}