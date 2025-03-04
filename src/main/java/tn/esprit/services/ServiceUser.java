//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package tn.esprit.services;

import tn.esprit.models.ROLE;
import tn.esprit.models.User;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tn.esprit.utils.MyDatabase;

public class ServiceUser {
    private Connection con = MyDatabase.getInstance().getCnx();

    public ServiceUser() {
        if (this.con != null) {
            System.out.println("✅ Connexion réussie à la base de données !");
        } else {
            System.out.println("❌ Échec de la connexion à la base de données !");
        }

    }

    public void add(User user) {
        String qry = "INSERT INTO user (NomUser, PrenomUser, DateNaissanceUser, AdresseUser, TelephoneUser, EmailUser, Password, role, isActive) VALUES (?,?,?,?,?,?,?,?,?)";

        try (PreparedStatement pstm = this.con.prepareStatement(qry)) {
            // Set parameters
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());
            pstm.setDate(3, user.getDateNaissanceUser() != null ? Date.valueOf(user.getDateNaissanceUser()) : null);
            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getPassword());
            pstm.setString(8, user.getRole().name());
            pstm.setBoolean(9, user.isActive());

            // Execute the query and check the result
            int rowsAffected = pstm.executeUpdate();
            this.con.commit();

            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur ajouté avec succès !");
            } else {
                System.out.println("⚠️ Aucune ligne insérée !");
            }
        } catch (SQLException e) {
            try {
                // In case of error, rollback the transaction
                if (this.con != null) {
                    this.con.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("❌ Erreur lors du rollback de la transaction : " + rollbackEx.getMessage());
            }
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }


    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String qry = "SELECT * FROM user";

        try (Statement stm = this.con.createStatement();
             ResultSet rs = stm.executeQuery(qry)) {

            // Parcours du ResultSet pour mapper les utilisateurs
            while (rs.next()) {
                users.add(this.mapResultSetToUser(rs));
            }

        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }


    public boolean update(User user) {
        String qry = "UPDATE user SET NomUser=?, PrenomUser=?, DateNaissanceUser=?, AdresseUser=?, TelephoneUser=?, EmailUser=?, Password=?, Role=?, isActive=? WHERE ID_User=?";

        try (PreparedStatement pstm = this.con.prepareStatement(qry)) {
            // Set parameters
            pstm.setString(1, user.getNomUser());
            pstm.setString(2, user.getPrenomUser());
            pstm.setDate(3, user.getDateNaissanceUser() != null ? Date.valueOf(user.getDateNaissanceUser()) : null);
            pstm.setString(4, user.getAdresseUser());
            pstm.setString(5, user.getTelephoneUser());
            pstm.setString(6, user.getEmailUser());
            pstm.setString(7, user.getPassword());
            pstm.setString(8, user.getRole().name());
            pstm.setBoolean(9, user.isActive());
            pstm.setInt(10, user.getIdUser());

            // Execute the update query and check the result
            int rowsAffected = pstm.executeUpdate();
            this.con.commit();

            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
                return true;
            } else {
                System.out.println("⚠️ Aucun utilisateur mis à jour !");
                return false;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            try {
                // Rollback in case of an error
                if (this.con != null) {
                    this.con.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("❌ Erreur lors du rollback de la transaction : " + rollbackEx.getMessage());
            }
            return false;
        }
    }


    public void delete(int idUser) {
        String qry = "DELETE FROM user WHERE ID_User=?";

        try (PreparedStatement pstm = this.con.prepareStatement(qry)) {
            // Set the parameter
            pstm.setInt(1, idUser);

            // Execute the delete query
            int rowsAffected = pstm.executeUpdate();
            this.con.commit();

            if (rowsAffected > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur supprimé !");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
            try {
                // Rollback in case of an error
                if (this.con != null) {
                    this.con.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("❌ Erreur lors du rollback de la transaction : " + rollbackEx.getMessage());
            }
        }
    }


    public User getById(int idUser) {
        String qry = "SELECT * FROM user WHERE ID_User = ?";

        try (PreparedStatement pstm = this.con.prepareStatement(qry)) {
            // Set the parameter
            pstm.setInt(1, idUser);

            // Execute the query and retrieve the result set
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return this.mapResultSetToUser(rs);
                }
            } catch (SQLException e) {
                System.out.println("❌ Erreur lors de la récupération de l'utilisateur par ID : " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de l'exécution de la requête : " + e.getMessage());
        }

        return null;
    }

    public void toggleUserStatus(int idUser, boolean status) {
        String qry = "UPDATE user SET isActive=? WHERE ID_User=?";

        try (PreparedStatement pstm = this.con.prepareStatement(qry)) {
            // Set the parameters
            pstm.setBoolean(1, status);
            pstm.setInt(2, idUser);

            // Execute the update
            int rowsAffected = pstm.executeUpdate();
            this.con.commit();

            if (rowsAffected > 0) {
                System.out.println("✅ Statut de l'utilisateur mis à jour !");
            } else {
                System.out.println("⚠️ Aucun utilisateur mis à jour ! Vérifie l'ID.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erreur lors de la mise à jour du statut de l'utilisateur : " + e.getMessage());
        }
    }


    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setIdUser(rs.getInt("ID_User"));
        user.setNomUser(rs.getString("NomUser"));
        user.setPrenomUser(rs.getString("PrenomUser"));
        user.setDateNaissanceUser(rs.getDate("DateNaissanceUser") != null ? rs.getDate("DateNaissanceUser").toLocalDate() : null);
        user.setAdresseUser(rs.getString("AdresseUser"));
        user.setTelephoneUser(rs.getString("TelephoneUser"));
        user.setEmailUser(rs.getString("EmailUser"));
        user.setPassword(rs.getString("Password"));
        user.setRole(ROLE.valueOf(rs.getString("Role")));
        user.setActive(rs.getBoolean("isActive"));
        return user;
    }


}
