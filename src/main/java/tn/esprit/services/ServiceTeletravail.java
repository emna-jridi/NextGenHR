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

    // ✅ Ajouter un télétravail
    public boolean add(Teletravail teletravail) {
        return executeTransaction(() -> executeUpdate(INSERT_SQL, teletravail.getIdEmploye(), Date.valueOf(teletravail.getDateDemandeTT()), Date.valueOf(teletravail.getDateDebutTT()), Date.valueOf(teletravail.getDateFinTT()), teletravail.getStatutTT(), teletravail.getRaisonTT()));
    }

    // ✅ Mettre à jour un télétravail
    public boolean update(Teletravail teletravail) {
        boolean result = executeTransaction(() -> executeUpdate(UPDATE_SQL, teletravail.getIdEmploye(), Date.valueOf(teletravail.getDateDemandeTT()), Date.valueOf(teletravail.getDateDebutTT()), Date.valueOf(teletravail.getDateFinTT()), teletravail.getStatutTT(), teletravail.getRaisonTT(), teletravail.getIdTeletravail()));
        if (result) cache.put(teletravail.getIdTeletravail(), teletravail); // Mise à jour du cache
        return result;
    }

    // ✅ Supprimer un télétravail
    public boolean delete(int idTeletravail) {
        boolean result = executeTransaction(() -> executeUpdate(DELETE_SQL, idTeletravail));
        if (result) cache.remove(idTeletravail); // Supprimer du cache
        return result;
    }

    // ✅ Récupérer un télétravail par ID (avec cache)
    public Teletravail getById(int id) {
        if (cache.containsKey(id)) {
            LOGGER.log(Level.INFO, "Récupéré depuis le cache.");
            return cache.get(id);
        }
        Teletravail teletravail = executeQuery(SELECT_BY_ID_SQL, rs -> {
            return new Teletravail(
                    rs.getInt("IdEmploye"),
                    rs.getDate("DateDemandeTT").toLocalDate(),
                    rs.getDate("DateDebutTT").toLocalDate(),
                    rs.getDate("DateFinTT").toLocalDate(),
                    rs.getString("StatutTT"),
                    rs.getString("RaisonTT")
            );
        }, id);
        if (teletravail != null) cache.put(id, teletravail); // Ajouter au cache
        return teletravail;
    }

    // ✅ Récupérer tous les télétravails
    public List<Teletravail> getAll() {
        List<Teletravail> teletravails = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                Teletravail teletravail = new Teletravail(
                        rs.getInt("IdEmploye"),
                        rs.getDate("DateDemandeTT").toLocalDate(),
                        rs.getDate("DateDebutTT").toLocalDate(),
                        rs.getDate("DateFinTT").toLocalDate(),
                        rs.getString("StatutTT"),
                        rs.getString("RaisonTT")
                );
                teletravail.setIdTeletravail(rs.getInt("IdTeletravail"));
                teletravails.add(teletravail);
                cache.put(teletravail.getIdTeletravail(), teletravail); // Mise à jour du cache
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des télétravails : " + e.getMessage(), e);
        }
        return teletravails;
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
