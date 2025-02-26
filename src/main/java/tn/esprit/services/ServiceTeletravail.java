package tn.esprit.services;

import tn.esprit.models.Teletravail;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;

public class ServiceTeletravail {

    private Connection connection;

    public ServiceTeletravail() {
        connection = MyDatabase.getInstance().getCnx();
    }

    private boolean employeExists(int idEmploye) {
        String sql = "SELECT 1 FROM employés WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public List<Teletravail> getAllSortedById() {
        List<Teletravail> teletravails = getAll();
        return teletravails.stream()
                .sorted(Comparator.comparingInt(Teletravail::getIdTeletravail))
                .collect(Collectors.toList());
    }
    public boolean add(Teletravail teletravail) {
        if (!employeExists(teletravail.getIdEmploye())) {
            System.out.println("L'employé n'existe pas.");
            return false;
        }

        String sql = "INSERT INTO teletravail (IdEmploye, DateDemandeTT, DateDebutTT, DateFinTT, StatutTT, RaisonTT) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pst.setInt(1, teletravail.getIdEmploye());
            pst.setDate(2, Date.valueOf(teletravail.getDateDemandeTT()));
            pst.setDate(3, Date.valueOf(teletravail.getDateDebutTT()));
            pst.setDate(4, Date.valueOf(teletravail.getDateFinTT()));
            pst.setString(5, teletravail.getStatutTT());
            pst.setString(6, teletravail.getRaisonTT());

            int affectedRows = pst.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pst.getGeneratedKeys()) {
                    if (rs.next()) {
                        teletravail.setIdTeletravail(rs.getInt(1)); // Récupère l'ID généré
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean update(Teletravail teletravail) {
        String sql = "UPDATE teletravail SET IdEmploye = ?, DateDemandeTT = ?, DateDebutTT = ?, DateFinTT = ?, StatutTT = ?, RaisonTT = ? WHERE IdTeletravail = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, teletravail.getIdEmploye());
            pst.setDate(2, Date.valueOf(teletravail.getDateDemandeTT()));
            pst.setDate(3, Date.valueOf(teletravail.getDateDebutTT()));
            pst.setDate(4, Date.valueOf(teletravail.getDateFinTT()));
            pst.setString(5, teletravail.getStatutTT());
            pst.setString(6, teletravail.getRaisonTT());
            pst.setInt(7, teletravail.getIdTeletravail());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(int idTeletravail) {
        String sql = "DELETE FROM teletravail WHERE IdTeletravail = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idTeletravail);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Teletravail getById(int id) {
        String sql = "SELECT * FROM teletravail WHERE IdTeletravail = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Teletravail(
                            rs.getInt("IdTeletravail"),
                            rs.getInt("IdEmploye"),
                            rs.getDate("DateDemandeTT").toLocalDate(),
                            rs.getDate("DateDebutTT").toLocalDate(),
                            rs.getDate("DateFinTT").toLocalDate(),
                            rs.getString("StatutTT"),
                            rs.getString("RaisonTT")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Teletravail> getAll() {
        List<Teletravail> teletravails = new ArrayList<>();
        String sql = "SELECT * FROM teletravail";
        try (Statement st = connection.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                teletravails.add(new Teletravail(
                        rs.getInt("IdTeletravail"),
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateDemandeTT").toLocalDate(),
                        rs.getDate("DateDebutTT").toLocalDate(),
                        rs.getDate("DateFinTT").toLocalDate(),
                        rs.getString("StatutTT"),
                        rs.getString("RaisonTT")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teletravails;
    }

    public String getEmployeeName(int idEmploye) {
        String sql = "SELECT PrenomEmploye FROM employés WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PrenomEmploye");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Inconnu";
    }

    public boolean incrementNbTTValide(int idEmploye) {
        String sql = "UPDATE employés SET NbTTValidé = NbTTValidé + 1 WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean incrementNbTTRefuse(int idEmploye) {
        String sql = "UPDATE employés SET NbTTRefusé = NbTTRefusé + 1 WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getEmployeeTTStats(int idEmploye) {
        String sql = "SELECT " +
                "SUM(CASE WHEN StatutTT = 'Approuvé' THEN 1 ELSE 0 END) AS nbValide, " +
                "SUM(CASE WHEN StatutTT = 'Refusé' THEN 1 ELSE 0 END) AS nbRefuse " +
                "FROM teletravail " +
                "WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int nbValide = rs.getInt("nbValide");
                    int nbRefuse = rs.getInt("nbRefuse");
                    return "Validé: " + nbValide + ", Refusé: " + nbRefuse;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Validé: 0, Refusé: 0";
    }

    public List<Teletravail> getTeletravailByEmploye(int idEmploye) {
        List<Teletravail> demandes = new ArrayList<>();
        String sql = "SELECT * FROM teletravail WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    demandes.add(new Teletravail(
                            rs.getInt("IdTeletravail"),
                            rs.getInt("IdEmploye"),
                            rs.getDate("DateDemandeTT").toLocalDate(),
                            rs.getDate("DateDebutTT").toLocalDate(),
                            rs.getDate("DateFinTT").toLocalDate(),
                            rs.getString("StatutTT"),
                            rs.getString("RaisonTT")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandes;
    }

    public String getNomEmploye(int idEmploye) {
        String sql = "SELECT PrenomEmploye, NomEmploye FROM employés WHERE IdEmploye = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PrenomEmploye") + " " + rs.getString("NomEmploye");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Inconnu";
    }
}
