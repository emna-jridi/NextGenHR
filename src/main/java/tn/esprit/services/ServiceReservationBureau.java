package tn.esprit.services;

import tn.esprit.models.ReservationBureau;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class ServiceReservationBureau {
    private final Connection cnx = MyDatabase.getInstance().getCnx();

    public boolean add(ReservationBureau reservation) {
        String sql = "INSERT INTO reservation_bureau (IdEmploye, IdBureau, DateReservation, DureeReservation, StatutReservation) VALUES (?, ?, ?, ?, ?)";
        return executeUpdate(sql, reservation, false);
    }

    public boolean update(ReservationBureau reservation) {
        String sql = "UPDATE reservation_bureau SET IdEmploye = ?, IdBureau = ?, DateReservation = ?, DureeReservation = ?, StatutReservation = ? WHERE IdReservation = ?";
        return executeUpdate(sql, reservation, true);
    }

    public boolean delete(int idReservation) {
        String sql = "DELETE FROM reservation_bureau WHERE IdReservation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idReservation);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public ReservationBureau getById(int id) {
        String sql = "SELECT * FROM reservation_bureau WHERE IdReservation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return mapResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ReservationBureau> getAll() {
        List<ReservationBureau> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation_bureau";
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                reservations.add(mapResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private boolean executeUpdate(String sql, ReservationBureau reservation, boolean includeId) {
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, reservation.getIdEmploye());
            pst.setInt(2, reservation.getIdBureau());
            pst.setDate(3, Date.valueOf(reservation.getDateReservation()));
            pst.setTime(4, Time.valueOf(reservation.getDureeReservation()));
            pst.setString(5, reservation.getStatutReservation());
            if (includeId) {
                pst.setInt(6, reservation.getIdReservation());
            }
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private ReservationBureau mapResultSet(ResultSet rs) throws SQLException {
        return new ReservationBureau(
                rs.getInt("IdReservation"),
                rs.getInt("IdEmploye"),
                rs.getInt("IdBureau"),
                rs.getDate("DateReservation").toLocalDate(),
                rs.getTime("DureeReservation").toLocalTime(),
                rs.getString("StatutReservation")
        );
    }
}
