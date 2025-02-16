package services;
import entities.Reunion;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceReunion {
        private Connection con;

        public ServiceReunion() {
            con = DBConnection.getInstance().getCon();
        }

        public void add(Reunion reunion) {
            String qry = "INSERT INTO reunion (titre, date , type, description) VALUES (?, ?, ?, ?)";
            try {
                PreparedStatement pstm = con.prepareStatement(qry);
                pstm.setString(1, reunion.getTitre());
                pstm.setDate(2, Date.valueOf(reunion.getDate()));
                pstm.setString(3, reunion.getType());
                pstm.setString(4, reunion.getDescription());
                int rowsInserted = pstm.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("✅ Réunion ajoutée avec succès !");
                } else {
                    System.out.println("❌ Aucune ligne insérée !");
                }
            } catch (SQLException e) {
                System.out.println("❌ Erreur SQL lors de l'ajout : " + e.getMessage());
            }
        }




        public List<Reunion> getAll() {
            List<Reunion> reunions = new ArrayList<>();
            String qry = "SELECT * FROM `reunion`";
            try {
                Statement stm = con.createStatement();
                ResultSet rs = stm.executeQuery(qry);
                while (rs.next()) {
                    Reunion r = new Reunion();
                    r.setId(rs.getInt("id"));
                    r.setTitre(rs.getString("titre"));
                    r.setDescription(rs.getString("description"));
                    r.setDate(rs.getDate("date").toLocalDate());
                    r.setType(rs.getString("type"));
                    reunions.add(r);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return reunions;
        }


        public void update(Reunion reunion, int id) {
            String qry = "UPDATE `reunion` SET `titre`=?, `description`=?, `date`=?, `type`=? WHERE `id`=?";
            try {
                PreparedStatement pstm = con.prepareStatement(qry);
                pstm.setString(1, reunion.getTitre());
                pstm.setString(2, reunion.getDescription());
                pstm.setDate(3, Date.valueOf(reunion.getDate()));
                pstm.setString(4, reunion.getType());
                pstm.setInt(5, id);
                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }


        public void delete(int id) {
            String qry = "DELETE FROM `reunion` WHERE `id`=?";
            try {
                PreparedStatement pstm = con.prepareStatement(qry);
                pstm.setInt(1, id);
                pstm.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }


