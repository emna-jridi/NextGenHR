package tn.esprit.services;

import tn.esprit.models.ReservationBureau;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceReservationBureau {

    private static final Logger LOGGER = Logger.getLogger(ServiceReservationBureau.class.getName());
    private static final String INSERT_SQL = "INSERT INTO reservation_bureau (IdEmploye, IdBureau, DateReservation, DureeReservation, StatutReservation) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE reservation_bureau SET IdEmploye = ?, IdBureau = ?, DateReservation = ?, DureeReservation = ?, StatutReservation = ? WHERE IdReservation = ?";
    private static final String DELETE_SQL = "DELETE FROM reservation_bureau WHERE IdReservation = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM reservation_bureau WHERE IdReservation = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM reservation_bureau";

    private final Connection cnx;
    private final Map<Integer, ReservationBureau> cache = new HashMap<>();

    public ServiceReservationBureau() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // Ajouter une réservation
    public boolean add(ReservationBureau reservation) {
        try (PreparedStatement pst = cnx.prepareStatement(INSERT_SQL)) {
            pst.setInt(1, reservation.getIdEmploye());
            pst.setInt(2, reservation.getIdBureau());
            pst.setDate(3, Date.valueOf(reservation.getDateReservation()));
            pst.setTime(4, Time.valueOf(reservation.getDureeReservation()));
            pst.setString(5, reservation.getStatutReservation());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'ajout de la réservation", e);
            return false;
        }
    }

    // Mettre à jour une réservation
    public boolean update(ReservationBureau reservation) {
        try (PreparedStatement pst = cnx.prepareStatement(UPDATE_SQL)) {
            pst.setInt(1, reservation.getIdEmploye());
            pst.setInt(2, reservation.getIdBureau());
            pst.setDate(3, Date.valueOf(reservation.getDateReservation()));
            pst.setTime(4, Time.valueOf(reservation.getDureeReservation()));
            pst.setString(5, reservation.getStatutReservation());
            pst.setInt(6, reservation.getIdReservation());
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la mise à jour de la réservation", e);
            return false;
        }
    }

    // Supprimer une réservation
    public boolean delete(int idReservation) {
        try (PreparedStatement pst = cnx.prepareStatement(DELETE_SQL)) {
            pst.setInt(1, idReservation);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la suppression de la réservation", e);
            return false;
        }
    }

    // Récupérer une réservation par ID (avec cache)
    public ReservationBureau getById(int id) {
        if (cache.containsKey(id)) {
            LOGGER.log(Level.INFO, "Récupéré depuis le cache.");
            return cache.get(id);
        }

        try (PreparedStatement pst = cnx.prepareStatement(SELECT_BY_ID_SQL)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    ReservationBureau reservation = new ReservationBureau(
                            rs.getInt("IdEmploye"),
                            rs.getInt("IdBureau"),
                            rs.getDate("DateReservation").toLocalDate(),
                            rs.getTime("DureeReservation").toLocalTime(),
                            rs.getString("StatutReservation")
                    );
                    reservation.setIdReservation(rs.getInt("IdReservation"));
                    cache.put(id, reservation); // Mettre à jour le cache
                    return reservation;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de la réservation", e);
        }
        return null;
    }

    // Récupérer toutes les réservations
    public List<ReservationBureau> getAll() {
        List<ReservationBureau> reservations = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                ReservationBureau reservation = new ReservationBureau(
                        rs.getInt("IdEmploye"),
                        rs.getInt("IdBureau"),
                        rs.getDate("DateReservation").toLocalDate(),
                        rs.getTime("DureeReservation").toLocalTime(),
                        rs.getString("StatutReservation")
                );
                reservation.setIdReservation(rs.getInt("IdReservation"));
                reservations.add(reservation);
                cache.put(reservation.getIdReservation(), reservation);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des réservations", e);
        }
        return reservations;
    }
}
