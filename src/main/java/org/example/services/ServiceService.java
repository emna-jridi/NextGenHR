package org.example.services;

import org.example.interfaces.IServices;
import org.example.models.Contrat;
import org.example.models.Service;
import org.example.utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceService implements IServices<Service> {
    private Connection cnx;

    public ServiceService() {
        cnx = MyDatabase.getInstance().getCnx();
    }




    //ajouter un service//
    @Override
    public void add(Service service) {
        String qry = "INSERT INTO services (NomService, DescriptionService, TypeService, DateDebutService, DateFinService, StatusService, IdContrat) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, service.getNomService());
            pstm.setString(2, service.getDescriptionService());
            pstm.setString(3, service.getTypeService());
            pstm.setDate(4, new java.sql.Date(service.getDateDebutService().getTime()));
            pstm.setDate(5, new java.sql.Date(service.getDateFinService().getTime()));
            pstm.setString(6, service.getStatusService());
            pstm.setInt(7, service.getIdContrat());

            pstm.executeUpdate();
            System.out.println("Service ajouté avec succés !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du service : " + e.getMessage());
        }
    }



    //afficher les services//
    @Override
    public List<Service> getAll() {
        List<Service> services = new ArrayList<>();
        String qry = "SELECT * FROM services";

        try {
            Statement stm = cnx.createStatement();
            ResultSet rs = stm.executeQuery(qry);

            while (rs.next()) {
                Service s = new Service();
                        s.setIdService(rs.getInt("IdService"));
                        s.setNomService(rs.getString("NomService"));
                        s.setDescriptionService(rs.getString("DescriptionService"));
                        s.setTypeService(rs.getString("TypeService"));
                        s.setDateDebutService(rs.getDate("DateDebutService"));
                        s.setDateFinService(rs.getDate("DateFinService"));
                        s.setStatusService(rs.getString("StatusService"));
                        s.setIdContrat(rs.getInt("IdContrat"));

                services.add(s);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des services : " + e.getMessage());
        }

        return services;
    }



    //mettre à jour service//
    @Override
    public void update(Service service) {
        String qry = "UPDATE services SET NomService = ?, DescriptionService = ?, TypeService = ?, DateDebutService = ?, DateFinService = ?, StatusService = ?, IdContrat = ? WHERE IdService = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setString(1, service.getNomService());
            pstm.setString(2, service.getDescriptionService());
            pstm.setString(3, service.getTypeService());
            pstm.setDate(4, new java.sql.Date(service.getDateDebutService().getTime()));
            pstm.setDate(5, new java.sql.Date(service.getDateFinService().getTime()));
            pstm.setString(6, service.getStatusService());
            pstm.setInt(7, service.getIdContrat());
            pstm.setInt(8, service.getIdService());

            pstm.executeUpdate();
            System.out.println("Service mis à jour !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour du service : " + e.getMessage());
        }
    }


    //supprimer service//
    @Override
    public void delete(Service service) {
        String qry = "DELETE FROM services WHERE IdService = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, service.getIdService());
            pstm.executeUpdate();
            System.out.println("Service supprimé avec succés !");
        } catch (SQLException e) {
            System.out.println("Erreur lors de la suppression du service : " + e.getMessage());
        }
    }



    // Récupérer un service par son ID
    public Service getById(int id) {
        String qry = "SELECT * FROM services WHERE IdService = ?";
        try {
            Connection conn = MyDatabase.getInstance().getCnx();
            PreparedStatement pstm = cnx.prepareStatement(qry);
            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                Service service = new Service();
                service.setIdService(rs.getInt("IdService"));
                service.setNomService(rs.getString("NomService"));
                service.setDescriptionService(rs.getString("DescriptionService"));
                service.setTypeService(rs.getString("TypeService"));
                service.setDateDebutService(rs.getDate("DateDebutService"));
                service.setDateFinService(rs.getDate("DateFinService"));
                service.setStatusService(rs.getString("StatusService"));
                service.setIdContrat(rs.getInt("IdContrat"));
                return service;
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération du service : " + e.getMessage());
        }
        return null; // Retourne null si aucun service n'est trouvé
    }



}
