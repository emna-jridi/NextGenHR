package tn.esprit.models;

import java.time.LocalDateTime;

public class TestAssignment {
    private int id;
    private int employee_id;
    private String nomUser;    // Ajoutez cet attribut

    private String test_category;
    private int assigned_by;
    private int questions_count;
    private LocalDateTime deadline;
    private String status;  // PENDING, COMPLETED, EXPIRED
    private String notes;
    private LocalDateTime created_at;
    private LocalDateTime 	updated_at;

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEmployeeId() {
        return employee_id;
    }

    public void setEmployeeId(int employee_id) {
        this.employee_id = employee_id;
    }
    public String getNomUser() {
        return nomUser;
    }

    public void setNomUser(String nomUser) {
        this.nomUser = nomUser;
    }

    public String getTestCategory() {
        return test_category;
    }

    public void setTestCategory(String test_category) {
        this.test_category = test_category;
    }

    public int getAssignedBy() {
        return assigned_by;
    }

    public void setAssignedBy(int assigned_by) {
        this.assigned_by = assigned_by;
    }

    public int getQuestionsCount() {
        return questions_count;
    }

    public void setQuestionsCount(int questions_count) {
        this.questions_count = questions_count;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdatedAt() {
        return updated_at;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "TestAssignment{" +
                "id=" + id +
                ", employee_id=" + employee_id +
                ", test_category='" + test_category + '\'' +
                ", assigned_by=" + assigned_by +
                ", questions_count=" + questions_count +
                ", deadline=" + deadline +
                ", status='" + status + '\'' +
                ", notes='" + notes + '\'' +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}