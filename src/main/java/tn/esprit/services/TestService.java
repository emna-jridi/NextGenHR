package tn.esprit.services;

import tn.esprit.models.Questions;
import tn.esprit.models.User;
import tn.esprit.models.TestAssignment;
import tn.esprit.models.TestResult;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class TestService {
    private QuizApiService quizApiService;

    private Connection cnx;

    public TestService() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    /**
     * Sauvegarde un résultat de test en base de données
     */
    public int saveTestResult(TestResult result) throws SQLException {
        String query = "INSERT INTO test_results (employee_id, test_category, total_questions, " +
                "correct_answers, score, analysis_notes, test_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement statement = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, result.getEmployeeId());
        statement.setString(2, result.getTestCategory());
        statement.setInt(3, result.getTotalQuestions());
        statement.setInt(4, result.getCorrectAnswers());
        statement.setDouble(5, result.getScore());
        statement.setString(6, result.getAnalysisNotes());
        statement.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));

        statement.executeUpdate();

        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            return rs.getInt(1);
        }
        return -1;
    }

    /**
     * Méthode complète pour exécuter un test et enregistrer les résultats
     */
    public TestResult executeTest(int employeeId, String category, Map<Integer, String> userAnswers) {
        try {
            // Récupérer les questions
            List<Questions> questions = quizApiService.fetchQuestions(category, 10); // Par défaut 10 questions

            // Vérifier les réponses et calculer le score
            TestResult result = quizApiService.checkAnswers(questions, userAnswers, employeeId, category);

            // Enregistrer le résultat
            int resultId = saveTestResult(result);
            if (resultId > 0) {
                result.setId(resultId);
                System.out.println("Test enregistré avec succès. ID: " + resultId);
            } else {
                System.err.println("Erreur lors de l'enregistrement du test.");
            }

            return result;
        } catch (Exception e) {
            System.err.println("Erreur lors de l'exécution du test: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    /**
     * Récupère la liste des résultats de test selon un filtre
     */
    public List<TestResult> getTestResults(String filterCategory, Integer filterEmployeeId) throws SQLException {
        StringBuilder query = new StringBuilder("SELECT * FROM test_results WHERE 1=1");

        if (filterCategory != null && !filterCategory.isEmpty()) {
            query.append(" AND test_category = ?");
        }
        if (filterEmployeeId != null) {
            query.append(" AND employee_id = ?");
        }

        PreparedStatement statement = cnx.prepareStatement(query.toString());

        int paramIndex = 1;
        if (filterCategory != null && !filterCategory.isEmpty()) {
            statement.setString(paramIndex++, filterCategory);
        }
        if (filterEmployeeId != null) {
            statement.setInt(paramIndex, filterEmployeeId);
        }

        ResultSet rs = statement.executeQuery();
        List<TestResult> results = new ArrayList<>();

        while (rs.next()) {
            TestResult result = new TestResult();
            result.setId(rs.getInt("id"));
            result.setEmployeeId(rs.getInt("employee_id"));
            result.setTestCategory(rs.getString("test_category"));
            result.setTotalQuestions(rs.getInt("total_questions"));
            result.setCorrectAnswers(rs.getInt("correct_answers"));
            result.setScore(rs.getDouble("score"));
            result.setAnalysisNotes(rs.getString("analysis_notes"));
            result.setTestDate(rs.getTimestamp("test_date").toLocalDateTime());

            results.add(result);
        }

        return results;
    }

    /**
     * Assigne un test à un employé
     */
    public void assignTestToEmployee(TestAssignment assignment) throws SQLException {
        String query = "INSERT INTO test_assignments (employee_id,nomEmployee, test_category, assigned_by, " +
                "questions_count, deadline, status, notes, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, assignment.getEmployeeId());
        statement.setString(2, assignment.getNomUser());
        statement.setString(3, assignment.getTestCategory());
        statement.setInt(4, assignment.getAssignedBy());
        statement.setInt(5, assignment.getQuestionsCount());
        statement.setTimestamp(6, Timestamp.valueOf(assignment.getDeadline()));
        statement.setString(7, assignment.getStatus());
        statement.setString(8, assignment.getNotes());
        statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));

        statement.executeUpdate();
    }

    /**
     * Récupère les tests assignés à un employé
     */
    public List<TestAssignment> getAssignedTests(int employeeId) throws SQLException {
        String query = "SELECT * FROM test_assignments WHERE employee_id = ? AND status = 'PENDING' " +
                "ORDER BY deadline ASC";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, employeeId);

        ResultSet rs = statement.executeQuery();
        List<TestAssignment> assignments = new ArrayList<>();

        while (rs.next()) {
            TestAssignment assignment = new TestAssignment();
            assignment.setId(rs.getInt("id"));
            assignment.setEmployeeId(rs.getInt("employee_id"));
            assignment.setTestCategory(rs.getString("test_category"));
            assignment.setAssignedBy(rs.getInt("assigned_by"));
            assignment.setQuestionsCount(rs.getInt("questions_count"));
            assignment.setDeadline(rs.getTimestamp("deadline").toLocalDateTime());
            assignment.setStatus(rs.getString("status"));
            assignment.setNotes(rs.getString("notes"));
            assignment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            assignments.add(assignment);
        }

        return assignments;
    }

    /**
     * Met à jour le statut d'un test assigné
     */
    public void updateTestAssignmentStatus(int assignmentId, String status) throws SQLException {
        String query = "UPDATE test_assignments SET status = ?, updated_at = ? WHERE id = ?";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setString(1, status);
        statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
        statement.setInt(3, assignmentId);

        statement.executeUpdate();
    }

    /**
     * Récupère les informations d'un employé par son ID
     */

    public User getEmployeeById(int employeeId) throws SQLException {
        String query = "SELECT * FROM `user` WHERE ID_User = ?";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, employeeId);

        ResultSet rs = statement.executeQuery();

        if (rs.next()) {
            User employee = new User();
            employee.setIdUser(rs.getInt("ID_User"));
            employee.setNomUser(rs.getString("NomUser"));
            employee.setPrenomUser(rs.getString("PrenomUser"));
            employee.setEmailUser(rs.getString("EmailUser"));

            return employee;
        }

        return null;
    }

    public List<TestAssignment> getAllAssignmentsByHR(int adminId) throws SQLException {
        String query = "SELECT * FROM test_assignments WHERE assigned_by = ? ORDER BY deadline ASC";

        PreparedStatement statement = cnx.prepareStatement(query);
        statement.setInt(1, adminId);

        ResultSet rs = statement.executeQuery();
        List<TestAssignment> assignments = new ArrayList<>();

        while (rs.next()) {
            TestAssignment assignment = new TestAssignment();
            assignment.setId(rs.getInt("id"));
            assignment.setEmployeeId(rs.getInt("employee_id"));
            assignment.setNomUser(rs.getString("nomEmployee"));
            assignment.setTestCategory(rs.getString("test_category"));
            assignment.setAssignedBy(rs.getInt("assigned_by"));
            assignment.setQuestionsCount(rs.getInt("questions_count"));
            assignment.setDeadline(rs.getTimestamp("deadline").toLocalDateTime());
            assignment.setStatus(rs.getString("status"));
            assignment.setNotes(rs.getString("notes"));
            assignment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            assignments.add(assignment);
        }

        return assignments;
    }
    public List<Map.Entry<Integer, String>> getAllEmployees() {

            List<Map.Entry<Integer, String>> employees = new ArrayList<>();
            String query = "SELECT ID_User, NomUser FROM user WHERE role = 'Employe'";



            try (
                    PreparedStatement stmt = cnx.prepareStatement(query);
                    ResultSet rs = stmt.executeQuery()) {



                int count = 0;
                while (rs.next()) {
                    int id = rs.getInt("ID_User");
                    String name = rs.getString("NomUser");


                    employees.add(new AbstractMap.SimpleEntry<>(id, name));
                    count++;
                }


            } catch (SQLException e) {

                return Collections.emptyList();
            }

            return employees;
        }

    public List<TestResult> getAllTestResults() {
        List<TestResult> results = new ArrayList<>();
        String query = "SELECT tr.*, CONCAT(u.prenom, ' ', u.nom) AS employee_name " +
                "FROM test_results tr " +
                "JOIN user u ON tr.employee_id = u.id";

        try (
             PreparedStatement pstmt = cnx.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TestResult result = new TestResult();
                result.setId(rs.getInt("id"));
                result.setEmployeeId(rs.getInt("employee_id"));
                result.setTestCategory(rs.getString("test_category"));
                result.setTotalQuestions(rs.getInt("total_questions"));
                result.setCorrectAnswers(rs.getInt("correct_answers"));
                result.setScore(rs.getDouble("score"));
                result.setAnalysisNotes(rs.getString("analysis_notes"));
                result.setTestDate(rs.getTimestamp("test_date").toLocalDateTime());
               // result.setEmployeeName(rs.getString("employee_name")); // Ajouter le nom de l'employé
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    // Récupérer les résultats d'un employé spécifique
    public List<TestResult> getTestResultsByEmployeeId(int employeeId) {
        List<TestResult> results = new ArrayList<>();
        String query = "SELECT tr.*, CONCAT(u.prenom, ' ', u.nom) AS employee_name " +
                "FROM test_results tr " +
                "JOIN user u ON tr.employee_id = u.id " +
                "WHERE tr.employee_id = ?";

        try (
             PreparedStatement pstmt = cnx.prepareStatement(query)) {

            pstmt.setInt(1, employeeId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                TestResult result = new TestResult();
                result.setId(rs.getInt("id"));
                result.setEmployeeId(rs.getInt("employee_id"));
                result.setTestCategory(rs.getString("test_category"));
                result.setTotalQuestions(rs.getInt("total_questions"));
                result.setCorrectAnswers(rs.getInt("correct_answers"));
                result.setScore(rs.getDouble("score"));
                result.setAnalysisNotes(rs.getString("analysis_notes"));
                result.setTestDate(rs.getTimestamp("test_date").toLocalDateTime());
                //result.setEmployeeName(rs.getString("employee_name")); // Ajouter le nom de l'employé
                results.add(result);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
        }
