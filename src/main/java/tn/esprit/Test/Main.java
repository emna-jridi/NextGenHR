package tn.esprit.Test;

import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;
import tn.esprit.Services.ServiceCandidature;
import tn.esprit.Services.ServiceOffre;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ServiceOffre serviceOffre = new ServiceOffre();

        ServiceCandidature serviceCandidature = new ServiceCandidature();

       /* serviceOffre.add(new Offreemploi(0, "Développeur Java", // titre
                "Développement d'applications Java", // description
                "3 ans", // expérience requise
                "Bac+7", // niveau d'études
                "Java, Spring", // compétences
                "CDI", // type de contrat
                "Tunis", // localisation
                "Français, Anglais", // niveau des langues
                LocalDateTime.now(), // date de création
                LocalDateTime.now().plusMonths(1), // date d'expiration
                "1")); // statut));)

        System.out.println(serviceOffre.getAll());

        Offreemploi offre = new Offreemploi();
        offre.setId(1); // L'ID de l'offre d'emploi à mettre à jour
        offre.setTitre("Développeur Java Senior");
        offre.setDescription("Nous recherchons un développeur Java avec 5 ans d'expérience.");
        offre.setExperiencerequise("5 ans");
        offre.setNiveauEtudes("Bac +5");
        offre.setCompetences("Java, Spring, Hibernate");
        offre.setTypecontrat("CDI");
        offre.setLocalisation("Tunis");
        offre.setNiveaulangues("Anglais courant");
        offre.setDateCreation(LocalDateTime.now());
        offre.setDateExpiration(LocalDateTime.now().plusMonths(2));
        offre.setStatut("Ouvert");
        offre.setCandidaturesrecues(10);

        // Appeler la méthode update
        serviceOffre.update(offre);


        int offreIdASupprimer = 1; // Remplace par l'ID réel

        serviceOffre.delete(offreIdASupprimer);*/



        Candidature candidature = new Candidature();
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut("En attente");
        candidature.setCvUrl("path/to/cv.pdf");
        candidature.setLettreMotivation("path/to/lettreMotivation.pdf");

        // Appel de la méthode add
        serviceCandidature.add(candidature);

        List<Candidature> candidatures = serviceCandidature.getAll();
        for (Candidature c : candidatures) {
            System.out.println("ID: " + c.getId());
            System.out.println("Date de candidature: " + c.getDateCandidature());
            System.out.println("Statut: " + c.getStatut());
            System.out.println("URL du CV: " + c.getCvUrl());
            System.out.println("Lettre de motivation: " + c.getLettreMotivation());
            System.out.println("-------------------------------------");
        }


        candidature.setId(1);  // Assurez-vous de mettre l'ID de la candidature à mettre à jour
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut("En cours");
        candidature.setCvUrl("https://exemple.com/cv");
        candidature.setLettreMotivation("Motivation pour le poste...");

        // Mettre à jour la candidature
        serviceCandidature.update(candidature);




        int candidatureId = 1;  // Remplacez par l'ID réel à supprimer

        // Supprimer la candidature
        serviceCandidature.delete(candidatureId);

    }

}


