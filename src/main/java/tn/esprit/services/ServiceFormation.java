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
        String qry = "INSERT INTO `formation`(  `NomFormation`, `ThemeFormation`, `description`, `lien_formation`,  `niveau_difficulte`, `statut`, `date`) VALUES (?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getThemeFormation());
            pstm.setString(3, formation.getDescription());
            pstm.setString(4,formation.getLien_formation());
           pstm.setString(5,formation.getNiveauDifficulte());
           pstm.setString(6, formation.getStatut());
            pstm.setDate(7, formation.getDateFormation());
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
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setStatut(rs.getString("statut"));
                f.setDateFormation(rs.getDate("date"));
                f.setIdFormation(rs.getInt("idFormation"));
                formations.add(f);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());


        }
        return formations;
    }

    @Override
    public void update(Formation formation) {

        String qry = "UPDATE `formation` SET `NomFormation`=?,`ThemeFormation`=?,`description`=?,`lien_formation`=?`niveau_difficulte`=?,`statut`=?,`date`=? WHERE  `IdFormation`= ? ";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, formation.getNomFormation());
            pstm.setString(2, formation.getThemeFormation());
            pstm.setString(3, formation.getDescription());
            pstm.setString(4, formation.getLien_formation());
            pstm.setString(5, formation.getNiveauDifficulte());
            pstm.setString(6,formation.getStatut());
            pstm.setDate(7, formation.getDateFormation());
            pstm.setInt(6, formation.getIdFormation());
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
        String qry = "SELECT * FROM `formation` WHERE `IdFormation`=?";
        try {
            PreparedStatement pstm = cnx.prepareStatement   (qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Formation f = new Formation();
                f.setNomFormation(rs.getString("NomFormation"));
                f.setThemeFormation(rs.getString("ThemeFormation"));
                f.setDescription(rs.getString("description"));
                f.setLien_formation(rs.getString("lien_formation"));
                f.setNiveauDifficulte(rs.getString("niveau_difficulte"));
                f.setStatut(rs.getString("statut"));
                f.setDateFormation(rs.getDate("date"));
                f.setIdFormation(rs.getInt("idFormation"));
                return f;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        }
        return null;
    }

}

