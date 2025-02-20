package tn.esprit.services;

import tn.esprit.models.Question;

import tn.esprit.interfaces.IService;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuestion implements IService<Question> {
    private Connection cnx;

    public ServiceQuestion() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Question question) {
        String query = "INSERT INTO question (texte, type, options, correct_response, test_id, points) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, question.getTexte());
            pst.setString(2, question.getType());
            pst.setString(3, question.getOptionsAsString());
            pst.setString(4, question.getCorrectResponse());
            pst.setInt(5, question.getTestId());
            pst.setInt(6, question.getPoints());
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) {
                question.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Question question) {
        String query = "UPDATE question SET texte=?, type=?, options=?, correct_response=?, points=? WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setString(1, question.getTexte());
            pst.setString(2, question.getType());
            pst.setString(3, question.getOptionsAsString());
            pst.setString(4, question.getCorrectResponse());
            pst.setInt(5, question.getPoints());
            pst.setInt(6, question.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Question question) {
        String query = "DELETE FROM question WHERE id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, question.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Question getById(int id) {
        return null;
    }

    @Override
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM question";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setTexte(rs.getString("texte"));
                q.setType(rs.getString("type"));
                q.setOptionsFromString(rs.getString("options"));
                q.setCorrectResponse(rs.getString("correct_response"));
                q.setTestId(rs.getInt("test_id"));
                q.setPoints(rs.getInt("points"));
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public List<Question> getByTestId(int testId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM question WHERE test_id=?";
        try (PreparedStatement pst = cnx.prepareStatement(query)) {
            pst.setInt(1, testId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Question q = new Question();
                q.setId(rs.getInt("id"));
                q.setTexte(rs.getString("texte"));
                q.setType(rs.getString("type"));
                q.setOptionsFromString(rs.getString("options"));
                q.setCorrectResponse(rs.getString("correct_response"));
                q.setTestId(rs.getInt("test_id"));
                q.setPoints(rs.getInt("points"));
                questions.add(q);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }
}