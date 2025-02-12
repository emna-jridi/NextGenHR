package tn.esprit.services;

import tn.esprit.models.Teletravail;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceTeletravail {

    private static final Logger LOGGER = Logger.getLogger(ServiceTeletravail.class.getName());
    private static final String INSERT_SQL = "INSERT INTO teletravail (IdEmploye, DateDemandeTT, DateDebutTT, DateFinTT, StatutTT, RaisonTT) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE teletravail SET IdEmploye = ?, DateDemandeTT = ?, DateDebutTT = ?, DateFinTT = ?, StatutTT = ?, RaisonTT = ? WHERE IdTeletravail = ?";
    private static final String DELETE_SQL = "DELETE FROM teletravail WHERE IdTeletravail = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM teletravail WHERE IdTeletravail = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM teletravail";

    private final Connection cnx;
    private final Map<Integer, Teletravail> cache = new HashMap<>(); // Cache simple

    public ServiceTeletravail() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public boolean add(Teletravail teletravail) {
        try (PreparedStatement pst = cnx.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, teletravail.getIdEmploye());
            pst.setDate(2, Date.valueOf(teletravail.getDateDemandeTT()));
            pst.setDate(3, Date.valueOf(teletravail.getDateDebutTT()));
            pst.setDate(4, Date.valueOf(teletravail.getDateFinTT()));
            pst.setString(5, teletravail.getStatutTT());
            pst.setString(6, teletravail.getRaisonTT());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = pst.getGeneratedKeys();
                if (rs.next()) {
                    teletravail.setIdTeletravail(rs.getInt(1)); // Récupérer l'ID généré
                }
                cache.put(teletravail.getIdTeletravail(), teletravail); // Mise à jour du cache
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout : " + e.getMessage(), e);
        }
        return false;
    }

    public boolean update(Teletravail teletravail) {
        try (PreparedStatement pst = cnx.prepareStatement(UPDATE_SQL)) {
            pst.setInt(1, teletravail.getIdEmploye());
            pst.setDate(2, Date.valueOf(teletravail.getDateDemandeTT()));
            pst.setDate(3, Date.valueOf(teletravail.getDateDebutTT()));
            pst.setDate(4, Date.valueOf(teletravail.getDateFinTT()));
            pst.setString(5, teletravail.getStatutTT());
            pst.setString(6, teletravail.getRaisonTT());
            pst.setInt(7, teletravail.getIdTeletravail());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                cache.put(teletravail.getIdTeletravail(), teletravail); // Mise à jour du cache
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour : " + e.getMessage(), e);
        }
        return false;
    }

    public boolean delete(int idTeletravail) {
        try (PreparedStatement pst = cnx.prepareStatement(DELETE_SQL)) {
            pst.setInt(1, idTeletravail);
            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                cache.remove(idTeletravail); // Suppression du cache
                return true;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression : " + e.getMessage(), e);
        }
        return false;
    }

    public Teletravail getById(int id) {
        if (cache.containsKey(id)) {
            return cache.get(id);
        }
        try (PreparedStatement pst = cnx.prepareStatement(SELECT_BY_ID_SQL)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                Teletravail teletravail = new Teletravail(
                        rs.getInt("IdTeletravail"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateDemandeTT").toLocalDate(),
                        rs.getDate("DateDebutTT").toLocalDate(),
                        rs.getDate("DateFinTT").toLocalDate(),
                        rs.getString("StatutTT"),
                        rs.getString("RaisonTT")
                );
                cache.put(id, teletravail);
                return teletravail;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération par ID : " + e.getMessage(), e);
        }
        return null;
    }

    public List<Teletravail> getAll() {
        List<Teletravail> teletravails = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                Teletravail teletravail = new Teletravail(
                        rs.getInt("IdTeletravail"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateDemandeTT").toLocalDate(),
                        rs.getDate("DateDebutTT").toLocalDate(),
                        rs.getDate("DateFinTT").toLocalDate(),
                        rs.getString("StatutTT"),
                        rs.getString("RaisonTT")
                );
                teletravails.add(teletravail);
                cache.put(teletravail.getIdTeletravail(), teletravail); // Mise à jour du cache
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de tous les télétravails : " + e.getMessage(), e);
        }
        return teletravails;
    }
}
