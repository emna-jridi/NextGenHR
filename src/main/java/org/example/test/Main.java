package org.example;

import org.example.models.Contrat;
import org.example.models.Service;
import org.example.services.ServiceContrat;
import org.example.services.ServiceService;

import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        // Initializing service classes
        ServiceContrat serviceContrat = new ServiceContrat();
        ServiceService serviceService = new ServiceService();

        // Example of creating and adding a new contract
        Contrat contrat1 = new Contrat("CDD", new Date(), new Date(), "Actif", 5000, "John Doe", "john.doe@example.com", 1);
        serviceContrat.add(contrat1);

        // Example of creating and adding a new service
        Service service1 = new Service("Web Development", "Developing a website for the client", "IT", new Date(), new Date(), "Active", 1);
        serviceService.add(service1);

        // Retrieve all contracts
        /*System.out.println("All contracts:");
        List<Contrat> contrats = serviceContrat.getAll();
        for (Contrat c : contrats) {
            System.out.println(c);
        }

        // Retrieve all services
        System.out.println("\nAll services:");
        List<Service> services = serviceService.getAll();
        for (Service s : services) {
            System.out.println(s);
        }

        // Update contract example
        contrat1.setMontantContrat(6000);
        serviceContrat.update(contrat1);
        System.out.println("\nUpdated contract: " + contrat1);

        // Update service example
        service1.setStatusService("Completed");
        serviceService.update(service1);
        System.out.println("\nUpdated service: " + service1);

        // Retrieve a contract by ID
        Contrat retrievedContrat = serviceContrat.getById(1);
        System.out.println("\nRetrieved contract by ID: " + retrievedContrat);

        // Retrieve a service by ID
        Service retrievedService = serviceService.getById(1);
        System.out.println("\nRetrieved service by ID: " + retrievedService);

        // Delete contract example
        serviceContrat.delete(contrat1);

        // Delete service example
        serviceService.delete(service1);

        System.out.println("\nContract and service deleted.");*/
    }
}
