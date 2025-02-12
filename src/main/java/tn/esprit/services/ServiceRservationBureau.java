package tn.esprit.services;

import tn.esprit.models.ReservationBureau;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceRservationBureau {

    private static final Logger LOGGER = Logger.getLogger(ServiceRservationBureau.class.getName());
    private static final String INSERT_SQL = "INSERT INTO reservation_bureau (IdEmploye, IdBureau, DateReservation, DureeReservation, StatutReservation) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE reservation_bureau SET IdEmploye = ?, IdBureau = ?, DateReservation = ?, DureeReservation = ?, StatutReservation = ? WHERE IdReservation = ?";
    private static final String DELETE_SQL = "DELETE FROM reservation_bureau WHERE IdReservation = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM reservation_bureau WHERE IdReservation = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM reservation_bureau";

    private final Connection cnx;
    private final Map<Integer, ReservationBureau> cache = new HashMap<>(); // ✅ Cache simple

    public ServiceRservationBureau() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // ✅ Ajouter une réservation
    public boolean add(ReservationBureau reservation) {
        return executeTransaction(() -> executeUpdate(INSERT_SQL, reservation.getIdEmploye(), reservation.getIdBureau(), reservation.getDateReservation(), reservation.getDureeReservation(), reservation.getStatutReservation()));
    }

    // ✅ Modifier une réservation
    public boolean update(ReservationBureau reservation) {
        boolean result = executeTransaction(() -> executeUpdate(UPDATE_SQL, reservation.getIdEmploye(), reservation.getIdBureau(), reservation.getDateReservation(), reservation.getDureeReservation(), reservation.getStatutReservation(), reservation.getIdReservation()));
        if (result) cache.put(reservation.getIdReservation(), reservation); // ✅ Mettre à jour le cache
        return result;
    }

    // ✅ Supprimer une réservation
    public boolean delete(int idReservation) {
        boolean result = executeTransaction(() -> executeUpdate(DELETE_SQL, idReservation));
        if (result) cache.remove(idReservation); // ✅ Supprimer du cache
        return result;
    }

    // ✅ Récupérer une réservation par ID (avec cache)
    public ReservationBureau getById(int id) {
        if (cache.containsKey(id)) {
            LOGGER.log(Level.INFO, "Récupéré depuis le cache.");
            return cache.get(id);
        }
        ReservationBureau reservation = executeQuery(SELECT_BY_ID_SQL, rs -> {
            LocalDate dateReservation = rs.getDate("DateReservation").toLocalDate();
            LocalTime dureeReservation = rs.getTime("DureeReservation").toLocalTime();

            ReservationBureau res = new ReservationBureau(
                    rs.getInt("IdEmploye"),
                    rs.getInt("IdBureau"),
                    dateReservation,
                    dureeReservation,
                    rs.getString("StatutReservation")
            );
            res.setIdReservation(rs.getInt("IdReservation"));
            return res;
        }, id);
        if (reservation != null) cache.put(id, reservation); // ✅ Ajouter au cache
        return reservation;
    }

    // ✅ Récupérer toutes les réservations
    public List<ReservationBureau> getAll() {
        List<ReservationBureau> reservations = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                LocalDate dateReservation = rs.getDate("DateReservation").toLocalDate();
                LocalTime dureeReservation = rs.getTime("DureeReservation").toLocalTime();

                ReservationBureau reservation = new ReservationBureau(
                        rs.getInt("IdEmploye"),
                        rs.getInt("IdBureau"),
                        dateReservation,
                        dureeReservation,
                        rs.getString("StatutReservation")
                );
                reservation.setIdReservation(rs.getInt("IdReservation"));
                reservations.add(reservation);
                cache.put(reservation.getIdReservation(), reservation); // ✅ Mettre à jour le cache
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des réservations : " + e.getMessage(), e);
        }
        return reservations;
    }

    private boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
            return pst.executeUpdate() > 0; // ✅ Retourne vrai si une ligne est affectée
        }
    }

    private <T> T executeQuery(String sql, ResultSetMapper<T> mapper, Object... params) {
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return mapper.map(rs);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'exécution de la requête : " + e.getMessage(), e);
        }
        return null;
    }

    private boolean executeTransaction(TransactionAction action) {
        try {
            cnx.setAutoCommit(false);
            boolean result = action.execute();
            cnx.commit(); // ✅ Commit explicite
            return result;
        } catch (SQLException e) {
            try {
                cnx.rollback(); // ✅ Rollback en cas d'erreur
                LOGGER.log(Level.SEVERE, "Transaction annulée : " + e.getMessage(), e);
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Erreur lors du rollback : " + rollbackEx.getMessage(), rollbackEx);
            }
            return false;
        } finally {
            try {
                cnx.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Erreur lors de la réactivation de l'auto-commit : " + e.getMessage(), e);
            }
        }
    }

    @FunctionalInterface
    private interface ResultSetMapper<T> {
        T map(ResultSet rs) throws SQLException;
    }

    @FunctionalInterface
    private interface TransactionAction {
        boolean execute() throws SQLException;
    }
}
