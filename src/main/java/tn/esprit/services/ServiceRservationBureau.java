package tn.esprit.services;

import tn.esprit.models.ReservationBureau;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceRservationBureau {

    private final Connection cnx;

    public ServiceRservationBureau() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    // ✅ Ajouter une réservation
    public void add(ReservationBureau reservation) {
        String sql = "INSERT INTO reservation_bureau (IdEmploye, IdBureau, DateReservation, DureeReservation, StatutReservation) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, reservation.getIdEmploye());
            pst.setInt(2, reservation.getIdBureau());
            pst.setDate(3, reservation.getDateReservation());
            pst.setTime(4, reservation.getDureeReservation());
            pst.setString(5, reservation.getStatutReservation());
            pst.executeUpdate();
            System.out.println("Réservation ajoutée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }

    // ✅ Modifier une réservation
    public void update(ReservationBureau reservation) {
        String sql = "UPDATE reservation_bureau SET IdEmploye = ?, IdBureau = ?, DateReservation = ?, DureeReservation = ?, StatutReservation = ? WHERE IdReservation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, reservation.getIdEmploye());
            pst.setInt(2, reservation.getIdBureau());
            pst.setDate(3, reservation.getDateReservation());
            pst.setTime(4, reservation.getDureeReservation());
            pst.setString(5, reservation.getStatutReservation());
            pst.setInt(6, reservation.getIdReservation());
            pst.executeUpdate();
            System.out.println("Réservation mise à jour avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour de la réservation : " + e.getMessage());
        }
    }




    // ✅ Supprimer une réservation
    public void delete(int idReservation) {
        String sql = "DELETE FROM reservation_bureau WHERE IdReservation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idReservation);
            pst.executeUpdate();
            System.out.println("Réservation supprimée avec succès !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    // ✅ Récupérer une réservation par ID
    public ReservationBureau getById(int id) {
        String sql = "SELECT * FROM reservation_bureau WHERE IdReservation = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new ReservationBureau(
                        rs.getInt("IdEmploye"),
                        rs.getInt("IdBureau"),
                        rs.getDate("DateReservation"),
                        rs.getTime("DureeReservation"),
                        rs.getString("StatutReservation")
                );
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération de la réservation : " + e.getMessage());
        }
        return null;
    }

    // ✅ Récupérer toutes les réservations
    public List<ReservationBureau> getAll() {
        List<ReservationBureau> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation_bureau";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                ReservationBureau reservation = new ReservationBureau(
                        rs.getInt("IdEmploye"),
                        rs.getInt("IdBureau"),
                        rs.getDate("DateReservation"),
                        rs.getTime("DureeReservation"),
                        rs.getString("StatutReservation")
                );
                reservation.setIdReservation(rs.getInt("IdReservation"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des réservations : " + e.getMessage());
        }
        return reservations;
    }
}
