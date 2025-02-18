package tn.esprit.test;

import tn.esprit.models.Contrat;
import tn.esprit.models.Service;
import tn.esprit.services.ServiceContrat;
import tn.esprit.services.ServiceService;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class MainServicesContrats {

    public static void main(String[] args) {


        // Initialiser services
        ServiceContrat serviceContrat = new ServiceContrat();
        ServiceService serviceService = new ServiceService();





        //////////////////////////////////////CRUD Contrats/////////////////////////////////////////////////////////

        Contrat contrat = new Contrat();

        // Add contrat
        //serviceContrat.add(new Contrat("Apprent", LocalDate.of(2025, 2, 18), LocalDate.of(2026, 2, 18), "Actif", 1000, "Jean Dupont", "jean.dupont@gmail.com"));


        // Retrieve contrats
        /*System.out.println("Liste des Contrats:");
        List<Contrat> contrats = serviceContrat.getAll();
        for (Contrat c : contrats) {
            System.out.println(c);
        }*/



        // Update contrat
        /*contrat.setIdContrat(6);
        contrat.setTypeContrat("stage3");
        contrat.setDateDebutContrat(LocalDate.of(2025, 2, 19));
        contrat.setDateFinContrat(LocalDate.of(2025, 2, 19));
        contrat.setStatusContrat("Inactif");
        contrat.setMontantContrat(600);
        contrat.setNomClient("alaa");
        contrat.setEmailClient("alaa@gmail.com");
        serviceContrat.update(contrat);*/



        // Retrieve contrat by ID
        /*Contrat retrievedContrat = serviceContrat.getById(1);
        System.out.println("\nRetrieved contract by ID: " + retrievedContrat);*/


        // Delete contrat
        //serviceContrat.delete(9);



        //recherche d'un contrat par le nom du client
        /*String clientName = "John Doe";
        List<Contrat> results = serviceContrat.searchByClientName(clientName);
        System.out.println("\nRésultats de la recherche pour le client: " + clientName);
        if (results.isEmpty()) {
            System.out.println("Aucun contrat trouvé pour ce client.");
        } else {
            for (Contrat c : results) {
                System.out.println(c);
            }
        }*/



        //tri des contrats par montant croissant
        /*System.out.println("\nContrats triés par montant (croissant) :");
        List<Contrat> contratsAsc = serviceContrat.sortByMontant(true);
        for (Contrat c : contratsAsc) {
            System.out.println(c);
        }*/


        // Tri des contrats par montant décroissant
        /*System.out.println("\nContrats triés par montant (décroissant) :");
        List<Contrat> contratsDesc = serviceContrat.sortByMontant(false);
        for (Contrat c : contratsDesc) {
            System.out.println(c);
        }*/



        // Filtrer et afficher les contrats actifs
        /*System.out.println("\nContrats actifs :");
        List<Contrat> activeContracts = serviceContrat.filterActiveContracts();
        for (Contrat c : activeContracts) {
            System.out.println(c);
        }*/



        //filtrage multi critéres des contrats par (status, minMontant, maxMontant,type et nomClient)
        /*System.out.println("Filtrage des contrats avec status 'actif' et montant entre 7000 et 8000 :");
        List<Contrat> contratsFiltres1 = serviceContrat.filterContrats("actif", 7000, 8000, null, null);
        if (contratsFiltres1.isEmpty()) {
            System.out.println("Aucun contrat trouvé.");
        } else {
            for (Contrat contrat1 : contratsFiltres1) {
                System.out.println(contrat1);
            }
        }
        System.out.println("\nFiltrage des contrats avec montant >= 3000 et type 'CDD' :");
        List<Contrat> contratsFiltres2 = serviceContrat.filterContrats(null, 3000, null, "CDD", null);
        if (contratsFiltres2.isEmpty()) {
            System.out.println("Aucun contrat trouvé.");
        } else {
            for (Contrat contrat1 : contratsFiltres2) {
                System.out.println(contrat1);
            }
        }
        System.out.println("\nFiltrage des contrats pour le client 'John Doe' :");
        List<Contrat> contratsFiltres3 = serviceContrat.filterContrats(null, null, null, null, "John Doe");
        if (contratsFiltres3.isEmpty()) {
            System.out.println("Aucun contrat trouvé.");
        } else {
            for (Contrat contrat1 : contratsFiltres3) {
                System.out.println(contrat1);
            }
        }*/



        //recupérer les services d’un contrat
        /*int idContrat = 1;
        List<Service> services = serviceContrat.getServicesByContrat(idContrat);
        System.out.println("Services du contrat " + idContrat + " :");
        for (Service s : services) {
            System.out.println(s);
        }*/

















/////////////////////////////////CRUD Services//////////////////////////////////////////////
        Service service = new Service();

        // ajouter service
        //serviceService.add(new Service("Sécurité informatique5", "Audit, prévention et gestion des risques en matière de cybersécurité.", "IT", LocalDate.of(2025, 2, 18), LocalDate.of(2025, 2, 18), "Inactif"));


        // Retrieve services
        /*System.out.println("\nAll services:");
        List<Service> services = serviceService.getAll();
        for (Service s : services) {
            System.out.println(s);
        }*/


        // Update service
        /*service.setIdService(8);
        service.setNomService("Sécurité informatique2");
        service.setDescriptionService("Audit, prévention et gestion des risques en matièr...");
        service.setTypeService("IT");
        service.setDateDebutService(LocalDate.of(2025, 2, 20));
        service.setDateFinService(LocalDate.of(2025, 2, 20));
        service.setStatusService("Inactif");
        //service.setIdContrat(4);
        serviceService.update(service);*/


        // Retrieve service by ID
        /*Service retrievedService = serviceService.getById(7);
        System.out.println("\nRetrieved service by ID: " + retrievedService);*/


        // Delete service
        //serviceService.delete(8);



        // Recherche des services par nom
        /*String serviceName = "Sécurité";
        List<Service> services = serviceService.getServicesByName(serviceName);
        if (services.isEmpty()) {
            System.out.println("Aucun service trouvé pour le nom : " + serviceName);
        } else {
            for (Service s : services) {
                System.out.println(s.toString());
            }
        }*/


        // Tri des services par type de service
        /*System.out.println("Services triés par type de service :");
        List<Service> sortedByType = serviceService.sortServicesByType();
        sortedByType.forEach(servicee -> System.out.println(servicee));*/



        // Filtrer les services actifs
        /*List<Service> activeServices = serviceService.filterActiveServices();
        if (activeServices.isEmpty()) {
            System.out.println("Aucun service actif trouvé.");
        } else {
            for (Service service3 : activeServices) {
                System.out.println(service3.toString());
            }
        }*/









    }
}
