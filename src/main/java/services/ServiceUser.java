package services;

import entities.User;
import entities.User.Role;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUser {

    private Connection con;

    public ServiceUser() {
        this.con = DBConnection.getInstance().getCon();
    }

    // ✅ Méthode pour ajouter un utilisateur avec le rôle
    public void add(User user) {
        String qry = "INSERT INTO user (NomUser, PrenomUser, DateNaissanceUser, AdresseUser, TelephoneUser, EmailUser, Role) VALUES (?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());
            pstm.setDate(3, Date.valueOf(user.getDateNaissanceUser()));
            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getRole().getDbValue()); // 🔥 Ajout du rôle

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // ✅ Méthode pour récupérer tous les utilisateurs
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM user";

        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                User u = new User();
                u.setIdUser(rs.getInt("ID_User"));
                u.setNomUser(rs.getString("NomUser"));
                u.setPrenomUser(rs.getString("PrenomUser"));
                u.setDateNaissanceUser(rs.getDate("DateNaissanceUser").toLocalDate());
                u.setAdresseUser(rs.getString("AdresseUser"));
                u.setTelephoneUser(rs.getString("TelephoneUser"));
                u.setEmailUser(rs.getString("EmailUser"));
                u.setRole(Role.fromDbValue(rs.getString("Role"))); // 🔥 Conversion en énumération

                users.add(u);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    // ✅ Méthode pour mettre à jour un utilisateur
    public void update(User user) {
        String qry = "UPDATE user SET NomUser=?, PrenomUser=?, DateNaissanceUser=?, AdresseUser=?, TelephoneUser=?, EmailUser=?, Role=? WHERE ID_User=?";
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());
            pstm.setDate(3, Date.valueOf(user.getDateNaissanceUser()));
            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getRole().getDbValue()); // 🔥 Mise à jour du rôle
            pstm.setInt(8, user.getIdUser());

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
        }
    }

    // ✅ Méthode pour supprimer un utilisateur par son ID
    public void delete(int idUser) {
        String qry = "DELETE FROM user WHERE ID_User=?";
        try {
            PreparedStatement pstm = con.prepareStatement(qry);
            pstm.setInt(1, idUser);

            pstm.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }
    }
}
