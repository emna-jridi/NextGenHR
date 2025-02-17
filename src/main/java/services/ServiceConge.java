package services;

import entities.conge;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceConge {

    private Connection con;

    public ServiceConge() {
        this.con = DBConnection.getInstance().getCon();
    }

    public void add(conge conge) {
        String qry = "INSERT INTO `conge`(`Type_conge`, `Date_debut`, `Date_fin`, `Status`) VALUES (?,?,?,?)";
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setString(1, conge.getType_conge());
            // Convert LocalDate to java.sql.Date properly
            pstm.setDate(2, Date.valueOf(conge.getDate_debut()));
            pstm.setDate(3, Date.valueOf(conge.getDate_fin()));
            pstm.setString(4, conge.getStatus());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<conge> getAll() {
        List<conge> conges = new ArrayList<>();
        String qry = "SELECT * FROM `conge`";
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(qry);
            while (rs.next()) {
                conge c = new conge();
                c.setId(rs.getInt("id"));
                c.setType_conge(rs.getString("Type_conge"));
                c.setDate_debut(rs.getDate("Date_debut").toLocalDate());
                c.setDate_fin(rs.getDate("Date_fin").toLocalDate());
                c.setStatus(rs.getString("Status"));
                conges.add(c);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Nombre de congés récupérés : " + conges.size());
        return conges;
    }


    public void update(conge conge, int id) {
        String qry = "UPDATE `conge` SET `Type_conge`=?, `Date_debut`=?, `Date_fin`=?, `Status`=? WHERE `id`=?";
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setString(1, conge.getType_conge());
            pstm.setDate(2, Date.valueOf(conge.getDate_debut()));
            pstm.setDate(3, Date.valueOf(conge.getDate_fin()));
            pstm.setString(4, conge.getStatus());
            pstm.setInt(5, id);

            int rowsAffected = pstm.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Congé mis à jour !");
            } else {
                System.out.println("Échec de la mise à jour !");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void delete(int id) {
        String qry = "DELETE FROM `conge` WHERE `id`=?";
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setInt(1, id);
            int rowsAffected = pstm.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Congé supprimé !");
            } else {
                System.out.println("Échec de la suppression !");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



}
