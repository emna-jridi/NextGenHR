package tn.esprit.services;

import tn.esprit.models.Teletravail;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
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
    private static final String CHECK_EMPLOYE_EXIST_SQL = "SELECT 1 FROM employés WHERE IdEmploye = ?";

    private final Connection cnx;
    private final Map<Integer, Teletravail> cache = new HashMap<>(); // Cache simple

    public ServiceTeletravail() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    private boolean isEmployeExist(int idEmploye) {
        try (PreparedStatement pst = cnx.prepareStatement(CHECK_EMPLOYE_EXIST_SQL)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next(); // Retourne true si l'employé existe
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la vérification de l'employé : " + e.getMessage(), e);
            return false;
        }
    }

    public boolean add(Teletravail teletravail) {
        if (!isEmployeExist(teletravail.getIdEmploye())) {
            LOGGER.log(Level.SEVERE, "L'employé avec ID " + teletravail.getIdEmploye() + " n'existe pas.");
            return false;
        }

        try (PreparedStatement pst = cnx.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
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
                        teletravail.setIdTeletravail(rs.getInt(1)); // Récupérer l'ID généré
                    }
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
            try (ResultSet rs = pst.executeQuery()) {
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

    public String getEmployeeName(int idEmploye) {
        String sql = "SELECT PrenomEmploye FROM employés WHERE IdEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("PrenomEmploye");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du nom de l'employé : " + e.getMessage(), e);
        }
        return "Inconnu";
    }


    public boolean incrementNbTTValide(int idEmploye) {
        String sql = "UPDATE employés SET NbTTValidé = NbTTValidé + 1 WHERE IdEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'incrémentation de NbTTValidé : " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Incrémente le compteur NbTTRefusé pour l'employé spécifié.
     *
     * @param idEmploye l'identifiant de l'employé
     * @return true si l'opération réussit, sinon false
     */
    public boolean incrementNbTTRefuse(int idEmploye) {
        String sql = "UPDATE employés SET NbTTRefusé = NbTTRefusé + 1 WHERE IdEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            int affectedRows = pst.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de l'incrémentation de NbTTRefusé : " + e.getMessage(), e);
        }
        return false;
    }

    /**
     * Récupère les compteurs de télétravail validé et refusé pour l'employé spécifié.
     *
     * @param idEmploye l'identifiant de l'employé
     * @return une chaîne de caractère au format "Validé: X, Refusé: Y"
     */
    public String getEmployeeTTStats(int idEmploye) {
        String sql = "SELECT " +
                "SUM(CASE WHEN StatutTT = 'Approuvé' THEN 1 ELSE 0 END) AS nbValide, " +
                "SUM(CASE WHEN StatutTT = 'Refusé' THEN 1 ELSE 0 END) AS nbRefuse " +
                "FROM teletravail " +
                "WHERE IdEmploye = ?";
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    int nbValide = rs.getInt("nbValide");
                    int nbRefuse = rs.getInt("nbRefuse");
                    return "Validé: " + nbValide + ", Refusé: " + nbRefuse;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du calcul des stats : " + e.getMessage(), e);
        }
        return "Validé: 0, Refusé: 0";
    }


    public Teletravail getTeletravailById(int idEmploye) {
        Teletravail teletravail = null;
        String sql = "SELECT * FROM teletravail WHERE IdEmploye = ?";  // Remplacez "teletravail" par le nom de votre table
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    teletravail = new Teletravail();
                    teletravail.setIdTeletravail(rs.getInt("IdTeletravail"));  // Remplacez par les bons noms de colonnes
                    teletravail.setIdEmploye(rs.getInt("IdEmploye"));
                    teletravail.setRaisonTT(rs.getString("RaisonTT"));
                    teletravail.setDateDemandeTT(rs.getDate("DateDemandeTT").toLocalDate());
                    teletravail.setDateDebutTT(rs.getDate("DateDebutTT").toLocalDate());
                    teletravail.setDateFinTT(rs.getDate("DateFinTT").toLocalDate());
                    teletravail.setStatutTT(rs.getString("StatutTT"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération du télétravail : " + e.getMessage(), e);
        }
        return teletravail;
    }


    public List<Teletravail> getTeletravailByEmploye(int idEmploye) {
        List<Teletravail> demandes = new ArrayList<>();
        String sql = "SELECT * FROM teletravail WHERE IdEmploye = ?";  // Remplacez par le bon nom de table
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            pst.setInt(1, idEmploye);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Teletravail teletravail = new Teletravail();
                    teletravail.setIdTeletravail(rs.getInt("IdTeletravail"));
                    teletravail.setIdEmploye(rs.getInt("IdEmploye"));
                    teletravail.setRaisonTT(rs.getString("RaisonTT"));
                    teletravail.setDateDemandeTT(rs.getDate("DateDemandeTT").toLocalDate());
                    teletravail.setDateDebutTT(rs.getDate("DateDebutTT").toLocalDate());
                    teletravail.setDateFinTT(rs.getDate("DateFinTT").toLocalDate());
                    teletravail.setStatutTT(rs.getString("StatutTT"));
                    demandes.add(teletravail);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des demandes : " + e.getMessage(), e);
        }
        return demandes;
    }

}