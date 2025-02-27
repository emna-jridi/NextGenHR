//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package services;

import entities.User;
import entities.User.Role;
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

import utils.DBConnection;

public class ServiceUser {
    private Connection con = DBConnection.getInstance().getCon();

    public ServiceUser() {
        if (this.con != null) {
            System.out.println("✅ Connexion réussie à la base de données !");
        } else {
            System.out.println("❌ Échec de la connexion à la base de données !");
        }

    }

    public void add(User user) {
        String qry = "INSERT INTO user (NomUser, PrenomUser, DateNaissanceUser, AdresseUser, TelephoneUser, EmailUser, Password, Role, isActive) VALUES (?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pstm = this.con.prepareStatement(qry);

            try {
                pstm.setString(1, user.getNomUser());
                pstm.setString(2, user.getPrenomUser());
                pstm.setDate(3, user.getDateNaissanceUser() != null ? Date.valueOf(user.getDateNaissanceUser()) : null);
                pstm.setString(4, user.getAdresseUser());
                pstm.setString(5, user.getTelephoneUser());
                pstm.setString(6, user.getEmailUser());
                pstm.setString(7, user.getPassword());
                pstm.setString(8, user.getRole().getDbValue());
                pstm.setBoolean(9, user.isActive());
                int rowsAffected = pstm.executeUpdate();
                this.con.commit();
                if (rowsAffected > 0) {
                    System.out.println("✅ Utilisateur ajouté avec succès !");
                } else {
                    System.out.println("⚠️ Aucune ligne insérée !");
                }
            } catch (Throwable var7) {
                if (pstm != null) {
                    try {
                        pstm.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException var8) {
            SQLException e = var8;
            System.out.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }

    }

    public List<User> getAll() {
        List<User> users = new ArrayList();
        String qry = "SELECT * FROM user";

        try {
            Statement stm = this.con.createStatement();

            try {
                ResultSet rs = stm.executeQuery(qry);

                try {
                    while(rs.next()) {
                        users.add(this.mapResultSetToUser(rs));
                    }
                } catch (Throwable var9) {
                    if (rs != null) {
                        try {
                            rs.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }

                    throw var9;
                }

                if (rs != null) {
                    rs.close();
                }
            } catch (Throwable var10) {
                if (stm != null) {
                    try {
                        stm.close();
                    } catch (Throwable var7) {
                        var10.addSuppressed(var7);
                    }
                }

                throw var10;
            }

            if (stm != null) {
                stm.close();
            }
        } catch (SQLException var11) {
            SQLException e = var11;
            System.out.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }

        return users;
    }

    public boolean update(User user) {
        String qry = "UPDATE user SET NomUser=?, PrenomUser=?, DateNaissanceUser=?, AdresseUser=?, TelephoneUser=?, EmailUser=?, Password=?, Role=?, isActive=? WHERE ID_User=?";

        try {
            PreparedStatement pstm = this.con.prepareStatement(qry);

            boolean var5;
            label54: {
                try {
                    pstm.setString(1, user.getNomUser());
                    pstm.setString(2, user.getPrenomUser());
                    pstm.setDate(3, user.getDateNaissanceUser() != null ? Date.valueOf(user.getDateNaissanceUser()) : null);
                    pstm.setString(4, user.getAdresseUser());
                    pstm.setString(5, user.getTelephoneUser());
                    pstm.setString(6, user.getEmailUser());
                    pstm.setString(7, user.getPassword());
                    pstm.setString(8, user.getRole().getDbValue());
                    pstm.setBoolean(9, user.isActive());
                    pstm.setInt(10, user.getIdUser());
                    int rowsAffected = pstm.executeUpdate();
                    this.con.commit();
                    if (rowsAffected > 0) {
                        System.out.println("✅ Utilisateur mis à jour avec succès !");
                        var5 = true;
                        break label54;
                    }

                    System.out.println("⚠️ Aucun utilisateur mis à jour !");
                    var5 = false;
                } catch (Throwable var7) {
                    if (pstm != null) {
                        try {
                            pstm.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (pstm != null) {
                    pstm.close();
                }

                return var5;
            }

            if (pstm != null) {
                pstm.close();
            }

            return var5;
        } catch (SQLException var8) {
            SQLException e = var8;
            System.out.println("❌ Erreur lors de la mise à jour de l'utilisateur : " + e.getMessage());
            return false;
        }
    }

    public void delete(int idUser) {
        String qry = "DELETE FROM user WHERE ID_User=?";

        try {
            PreparedStatement pstm = this.con.prepareStatement(qry);

            try {
                pstm.setInt(1, idUser);
                int rowsAffected = pstm.executeUpdate();
                this.con.commit();
                if (rowsAffected > 0) {
                    System.out.println("✅ Utilisateur supprimé avec succès !");
                } else {
                    System.out.println("⚠️ Aucun utilisateur supprimé !");
                }
            } catch (Throwable var7) {
                if (pstm != null) {
                    try {
                        pstm.close();
                    } catch (Throwable var6) {
                        var7.addSuppressed(var6);
                    }
                }

                throw var7;
            }

            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException var8) {
            SQLException e = var8;
            System.out.println("❌ Erreur lors de la suppression de l'utilisateur : " + e.getMessage());
        }

    }

    public User getById(int idUser) {
        String qry = "SELECT * FROM user WHERE ID_User = ?";

        try {
            PreparedStatement pstm = this.con.prepareStatement(qry);

            User var5;
            label54: {
                try {
                    pstm.setInt(1, idUser);
                    ResultSet rs = pstm.executeQuery();
                    if (rs.next()) {
                        var5 = this.mapResultSetToUser(rs);
                        break label54;
                    }
                } catch (Throwable var7) {
                    if (pstm != null) {
                        try {
                            pstm.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (pstm != null) {
                    pstm.close();
                }

                return null;
            }

            if (pstm != null) {
                pstm.close();
            }

            return var5;
        } catch (SQLException var8) {
            SQLException e = var8;
            System.out.println("❌ Erreur lors de la récupération de l'utilisateur par ID : " + e.getMessage());
            return null;
        }
    }

    public void toggleUserStatus(int idUser, boolean status) {
        String qry = "UPDATE user SET isActive=? WHERE ID_User=?";

        try {
            PreparedStatement pstm = this.con.prepareStatement(qry);

            try {
                pstm.setBoolean(1, status);
                pstm.setInt(2, idUser);
                int rowsAffected = pstm.executeUpdate();
                this.con.commit();
                if (rowsAffected > 0) {
                    System.out.println("✅ Statut de l'utilisateur mis à jour !");
                } else {
                    System.out.println("⚠️ Aucun utilisateur mis à jour ! Vérifie l'ID.");
                }
            } catch (Throwable var8) {
                if (pstm != null) {
                    try {
                        pstm.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }

                throw var8;
            }

            if (pstm != null) {
                pstm.close();
            }
        } catch (SQLException var9) {
            SQLException e = var9;
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
        user.setRole(Role.fromDbValue(rs.getString("Role")));
        user.setActive(rs.getBoolean("isActive"));
        return user;
    }


}
