package tn.esprit.Test;

import tn.esprit.Models.Candidature;
import tn.esprit.Models.Offreemploi;
import tn.esprit.Services.ServiceCandidature;
import tn.esprit.Services.ServiceOffre;
import tn.esprit.interfaces.IService;

import java.time.LocalDateTime;
import java.util.List;

public class MainCandidature {
    public static void main(String[] args) {

        ServiceOffre serviceOffre = new ServiceOffre();
        ServiceCandidature serviceCandidature = new ServiceCandidature();

        Offreemploi offreemploi = new Offreemploi();


//AJOUT SIMPLE
      /* serviceOffre.add(new Offreemploi(0, "Développeur ", // titre
                "Développement d'applications ", // description
                "3 ans", // expérience requise
                "Bac+7", // niveau d'études
                "Java, Spring", // compétences
                "CDI", // type de contrat
                "Tunis", // localisation
                "Français, Anglais", // niveau des langues
                LocalDateTime.now(), // date de création
                LocalDateTime.now().plusMonths(1), // date d'expiration
                "1")); // statut));)*/


        //Affichage du nombre de jours restants avant expiration
        offreemploi.setDateExpiration(LocalDateTime.of(2025, 2, 28, 23, 59, 59, 0));
        System.out.println("Jours restants avant expiration : " + offreemploi.getDaysRemaining());
        System.out.println(serviceOffre.getAll());


        //UPDATE
       /*
        offre.setId(2); // MAJ
        offre.setTitre("Développeur Java Senior");
        offre.setDescription("123");
        offre.setExperiencerequise("5 ans");
        offre.setNiveauEtudes("Bac +10");
        offre.setCompetences("Java, Spring");
        offre.setTypecontrat("CDI");
        offre.setLocalisation("Tunis");
        offre.setNiveaulangues("Anglais courant");
        offre.setDateCreation(LocalDateTime.now());
        offre.setDateExpiration(LocalDateTime.now().plusMonths(2));
        offre.setStatut("Ouvert");
        offre.setCandidaturesrecues(10);
        serviceOffre.update(offre);*/

//SUPPRESSION
      /* int offreIdASupprimer = 2; // Remplace par l'ID réel
        serviceOffre.delete(offreIdASupprimer);*/


        //GETBYID

        /*int idRechercheOFFRE = 4; // ID de l'offre à récupérer
offreemploi = serviceOffre.getbyid(idRechercheOFFRE);

        if (offreemploi != null) {
            System.out.println("Offre trouvée :");
            System.out.println("ID : " + offreemploi.getId());
            System.out.println("Titre : " + offreemploi.getTitre());
            System.out.println("Description : " + offreemploi.getDescription());
            System.out.println("comptences : " + offreemploi.getCompetences());
            System.out.println("Experience : " + offreemploi.getExperiencerequise());
            System.out.println("Niveau etude : " + offreemploi.getNiveauEtudes());
            System.out.println("NiveauLangues : " + offreemploi.getNiveaulangues());
            System.out.println("DateCreation : " + offreemploi.getDateCreation());
            System.out.println("Date d'expiration : " + offreemploi.getDateExpiration());
            System.out.println("Localisation : " + offreemploi.getLocalisation());
            System.out.println("TypeContrat : " + offreemploi.getTypecontrat());
            System.out.println("Candidatutesrecues :" + offreemploi.getCandidaturesrecues());
            System.out.println("Statut : " + offreemploi.getStatut());
        }
        else {
            System.out.println("Aucune offre trouvée avec l'ID " + idRechercheOFFRE);
        }*/





        //TAWA NEMCHIW LEL CANDIDATURE


        Candidature candidature = new Candidature();


        offreemploi.setId(7);
        candidature.setDateCandidature(LocalDateTime.now());         //ajout bel id offre
        candidature.setStatut("test test 1212");
        candidature.setCvUrl("http://exemple.com/cv.pdf");
        candidature.setLettreMotivation("Motivation pour ce poste nekdheb...");
        serviceCandidature.ajout(candidature, offreemploi.getId());








     /*  candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut("En attente");
        candidature.setCvUrl("path/to/cv.pdf");                                 //hedhi l add menghir offre
        candidature.setLettreMotivation("path/to/lettreMotivation.pdf");
        serviceCandidature.add(candidature);*/

        List<Candidature> candidatures = serviceCandidature.getAll();
        for (Candidature c : candidatures) {
            System.out.println("ID: " + c.getId());
            System.out.println("Date de candidature: " + c.getDateCandidature());
            System.out.println("Statut: " + c.getStatut());
            System.out.println("URL du CV: " + c.getCvUrl());
            System.out.println("Lettre de motivation: " + c.getLettreMotivation());
            System.out.println("-------------------------------------");

        }


//Lmise a jour
        /*candidature.setId(3);  // Assurez-vous de mettre l'ID de la candidature à mettre à jour
        candidature.setDateCandidature(LocalDateTime.now());
        candidature.setStatut("En coursssssss");
        candidature.setCvUrl("https://exemple.com/cv");
        candidature.setLettreMotivation("Motivation pour le poste...");
        serviceCandidature.update(candidature);*/

//SUPRESSION







        /*int idCandidature = 1;
        // Appel de la méthode delete
        serviceCandidature.delete(idCandidature);*/     //hedhi menghir id offre




        // ID de l'offre associée
        int idCandidature = 25; // ID de la candidature à supprimer
        int offreId = 7;
        serviceCandidature.delete(idCandidature, offreId);




//GETBYID
       /*int idRechercheCandid = 3; // ID de la candidature à récupérer
        candidature = serviceCandidature.getbyid(idRechercheCandid);

        if (candidature != null) {
            System.out.println("Candidature trouvée :");
            System.out.println("ID : " + candidature.getId());
            System.out.println("Date de candidature : " + candidature.getDateCandidature());
            System.out.println("Statut : " + candidature.getStatut());
            System.out.println("CV URL : " + candidature.getCvUrl());
            System.out.println("Lettre de motivation : " + candidature.getLettreMotivation());
        } else {
            System.out.println("Aucune candidature trouvée avec l'ID " + idRechercheCandid);
        }*/




       /* candidature.setId(2);  // ID de la candidature existante
        candidature.setDateCandidature(LocalDateTime.now()); // Mettre à jour la date
        candidature.setStatut("Acceptée"); // Nouveau statut
        candidature.setCvUrl("http://nouveau-cv-url.com"); // Nouveau CV
        candidature.setLettreMotivation("Nouvelle lettre de motivation mise à jour");
        //int offreId = 3;  // Supposons que l'offre associée a l'ID 2
        serviceCandidature.updatee(candidature, offreId);*/
    }


    }













