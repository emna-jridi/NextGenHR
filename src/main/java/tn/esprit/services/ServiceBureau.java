package tn.esprit.services;

import tn.esprit.models.Bureau;
import tn.esprit.utils.MyDatabase;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceBureau {

    private static final Logger LOGGER = Logger.getLogger(ServiceBureau.class.getName());
    private static final String INSERT_SQL = "INSERT INTO bureau (RefBureau, Capacite, Disponibilité) VALUES (?, ?, ?)";
    private static final String UPDATE_SQL = "UPDATE bureau SET RefBureau = ?, Capacite = ?, Disponibilité = ? WHERE IdBureau = ?";
    private static final String DELETE_SQL = "DELETE FROM bureau WHERE IdBureau = ?";
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM bureau WHERE IdBureau = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM bureau";

    private final Connection cnx;
    private final Map<Integer, Bureau> cache = new HashMap<>();

    public ServiceBureau() {
        cnx = MyDatabase.getInstance().getCnx();
    }

    public boolean add(Bureau bureau) {
        return executeTransaction(() -> executeUpdate(INSERT_SQL, bureau.getRefBureau(), bureau.getCapacite(), bureau.getDisponibilite()));
    }

    public boolean update(Bureau bureau) {
        boolean result = executeTransaction(() -> executeUpdate(UPDATE_SQL, bureau.getRefBureau(), bureau.getCapacite(), bureau.getDisponibilite(), bureau.getIdBureau()));
        if (result) cache.put(bureau.getIdBureau(), bureau);
        return result;
    }

    public boolean delete(int idBureau) {
        boolean result = executeTransaction(() -> executeUpdate(DELETE_SQL, idBureau));
        if (result) cache.remove(idBureau);
        return result;
    }

    public Bureau getById(int id) {
        if (cache.containsKey(id)) {
            LOGGER.log(Level.INFO, "Récupéré depuis le cache.");
            return cache.get(id);
        }
        Bureau bureau = executeQuery(SELECT_BY_ID_SQL, rs -> new Bureau(
                rs.getInt("IdBureau"),
                rs.getString("RefBureau"),
                rs.getInt("Capacite"),
                rs.getString("Disponibilité")
        ), id);
        if (bureau != null) cache.put(id, bureau);
        return bureau;
    }

    public List<Bureau> getAll() {
        List<Bureau> bureaux = new ArrayList<>();
        try (Statement st = cnx.createStatement(); ResultSet rs = st.executeQuery(SELECT_ALL_SQL)) {
            while (rs.next()) {
                Bureau bureau = new Bureau(
                        rs.getInt("IdBureau"),
                        rs.getString("RefBureau"),
                        rs.getInt("Capacite"),
                        rs.getString("Disponibilité")
                );
                bureaux.add(bureau);
                cache.put(bureau.getIdBureau(), bureau);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération des bureaux : " + e.getMessage(), e);
        }
        return bureaux;
    }

    private boolean executeUpdate(String sql, Object... params) throws SQLException {
        try (PreparedStatement pst = cnx.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                pst.setObject(i + 1, params[i]);
            }
            return pst.executeUpdate() > 0;
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
            cnx.commit();
            return result;
        } catch (SQLException e) {
            try {
                cnx.rollback();
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
