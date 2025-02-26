package tn.esprit.services;

import tn.esprit.interfaces.IService;
import tn.esprit.models.Formation;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceFormation implements IService<Formation> {
    private Connection cnx;

    public ServiceFormation() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    @Override
    public void add(Formation formation) {
        String qry = "INSERT INTO `formation`(`NomFormation`, `ThemeFormation`, `description`, `lien_formation`, " +
                "`niveau_difficulte`, `niveau`, `duree`, `image_url`, `date`) " +
                "VALUES (?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getThemeFormation());
            pstm.setString(3, formation.getDescription());
            pstm.setString(4, formation.getLien_formation());
            pstm.setString(5, formation.getNiveauDifficulte());
            pstm.setString(6, formation.getNiveau());
            pstm.setInt(7, formation.getDuree());
            pstm.setString(8, formation.getImageUrl());

            pstm.setDate(9, formation.getDateFormation());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Formation> getAll() {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation`";
        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return formations;
    }

    @Override
    public void update(Formation formation) {
        String qry = "UPDATE `formation` SET `NomFormation`=?, `ThemeFormation`=?, `description`=?, " +
                "`lien_formation`=?, `niveau_difficulte`=?, `niveau`=?, `duree`=?, `image_url`=?, " +
                " `date`=? WHERE `idFormation`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getThemeFormation());
            pstm.setString(3, formation.getDescription());
            pstm.setString(4, formation.getLien_formation());
            pstm.setString(5, formation.getNiveauDifficulte());
            pstm.setString(6, formation.getNiveau());
            pstm.setInt(7, formation.getDuree());
            pstm.setString(8, formation.getImageUrl());

            pstm.setDate(9, formation.getDateFormation());
            pstm.setInt(10, formation.getIdFormation());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delete(Formation formation) {
        String qry = "DELETE FROM `formation` WHERE `idFormation` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, formation.getIdFormation());
            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Formation getById(int id) {
        String qry = "SELECT * FROM `formation` WHERE `idFormation`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Méthode pour rechercher des formations par un texte dans le nom ou la description
    public List<Formation> searchFormations(String searchText) {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation` WHERE `NomFormation` LIKE ? OR `description` LIKE ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            String searchPattern = "%" + searchText + "%";
            pstm.setString(1, searchPattern);
            pstm.setString(2, searchPattern);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return formations;
    }

    // Méthode pour filtrer les formations par catégorie (statut)
    public List<Formation> getByCategory(String category) {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation` WHERE `ThemeFormation` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, category);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return formations;
    }

    // Méthode pour filtrer les formations par niveau
    public List<Formation> getByLevel(String level) {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation` WHERE `niveau` = ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, level);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return formations;
    }

    // Méthode pour filtrer les formations par durée
    public List<Formation> getByDuration(int minDuration, int maxDuration) {
        List<Formation> formations = new ArrayList<>();
        String qry = "SELECT * FROM `formation` WHERE `duree` BETWEEN ? AND ?";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, minDuration);
            pstm.setInt(2, maxDuration);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Formation f = new Formation();
                f.setIdFormation(rs.getInt("idFormation"));
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setNiveau(rs.getString("niveau"));
                f.setDuree(rs.getInt("duree"));
                f.setImageUrl(rs.getString("image_url"));
                f.setDateFormation(rs.getDate("date"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return formations;
    }
}