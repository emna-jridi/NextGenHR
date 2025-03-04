package tn.esprit.models;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ContratToText {

    public static String generateContract(Contrat contrat) {

        List<String> serviceNames = contrat.getServices().stream()
                .map(Service::getNomService)
                .collect(Collectors.toList());

        String servicesList = String.join(", ", serviceNames);

        return "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f8f9fa; padding: 20px; margin: 0; height: 100vh; display: flex; justify-content: center; align-items: center; }" +
                ".contract-container { max-width: 800px; background: white; padding: 30px; margin: auto; border: 2px solid #ddd; border-radius: 10px; box-shadow: 2px 2px 12px rgba(0, 0, 0, 0.1); position: relative; min-height: 90vh; display: flex; flex-direction: column; justify-content: space-between; }" +
                "h2 { text-align: center; color: #333; }" +
                "h3 { color: #555; border-bottom: 2px solid #ddd; padding-bottom: 5px; }" +
                "p { margin: 10px 0; }" +
                ".blue-text { color: blue; }" +
                ".signatures-container { display: flex; justify-content: space-between; margin-top: 50px; padding: 0 50px; }" +
                ".signature { text-align: center; width: 45%; }" +
                ".signature p { font-weight: bold; margin-bottom: 5px; }" +
                ".signature-box { display: flex; justify-content: center; align-items: center; border: 2px dashed #555; width: 220px; height: 60px; border-radius: 10px; background-color: rgba(0, 0, 0, 0.02); box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.1); }" +

                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='contract-container'>" +
                "<h2>CONTRAT DE TRAVAIL</h2>" +
                "<p><strong>Entre les soussignés :</strong></p>" +
                "<p><strong>La société :</strong> NextGenHR, Adresse: 123 Rue de l'Entreprise, Numéro SIRET: 987654321.</p>" +
                "<p><strong>Le/La Client(e) :</strong> <span class='blue-text'>" + contrat.getNomClient() + "</span> , <strong>Email:</strong> <span class='blue-text'>" + contrat.getEmailClient() + "</span> , <strong>Num Tel:</strong> <span class='blue-text'>" + contrat.getTelephoneClient() +  "</span> .</p>" +
                "<h3>PRÉAMBULE </h3>" +
                "<p>La société <strong>NextGenHR</strong> s'engage à fournir des services à <span class='blue-text'>" + contrat.getNomClient() + "</span> selon les termes et conditions décrites dans ce contrat. Le/la client(e) accepte les conditions énoncées dans le présent contrat pour bénéficier des services mentionnés ci-dessous : " + "</p>" +
                "<p>" +servicesList+ " " + ".</p>" +
                "<h3>Article 1 : Objet du contrat</h3>" +
                "<p>Le présent contrat a pour objet la fourniture des services suivants par la société <strong>NextGenHR</strong> au client(e) <span class='blue-text'>" + contrat.getNomClient() + "</span> " + ".</p>" +
                "<h3>Article 2 : Durée du Contrat</h3>" +
                "<p>Le présent contrat débute le <span class='blue-text'>" + contrat.getDateDebutContrat() + "</span> jusqu'au <span class='blue-text'>" + contrat.getDateFinContrat() + "</span> , sauf résiliation anticipée conformément aux dispositions de l'article 6 du présent contrat. " + ".</p>" +
                "<h3>Article 3 : Montant et Mode de Paiement</h3>" +
                "<p>En contrepartie des services fournis par la société <strong>NextGenHR</strong>, le/la client(e) s'engage à payer la somme suivante : <span class='blue-text'>" + contrat.getMontantContrat() +"</span> . Mode de paiement <span class='blue-text'>" + contrat.getModeDePaiement()  + "</span> .</p>" +
                "<h3>Article 4 : Obligations de la Société</h3>" +
                "<p>La société <strong>NextGenHR</strong> s'engage à :  Fournir les services définis dans le contrat dans les délais convenus, S'assurer de la qualité et de la conformité des services fournis, Fournir au client(e) toute l'assistance nécessaire pendant la durée du contrat " + ".</p>" +
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<h3>Article 5 : Obligations du Client</h3>" +
                "<p>Le/la client(e) s'engage à : Payer la somme convenue dans les délais prévus, Fournir à la société toutes les informations nécessaires à la bonne exécution des services, Respecter les conditions et instructions liées à l'exécution des services " + ".</p>" +
                "<h3>Article 6 : Résiliation du Contrat</h3>" +
                "<p>Le présent contrat peut être résilié de manière anticipée : Par le/la client(e), avec un préavis de 7 jours avant la date de résiliation, en cas de motif légitime, Par la société, en cas de non-paiement du client(e) ou de manquement grave de sa part aux obligations du contrat, En cas de force majeure, aucune des parties ne pourra être tenue responsable de l'exécution du contrat " + ".</p>" +
                "<h3>Article 7 : Confidentialité</h3>" +
                "<p>Les parties conviennent de garder strictement confidentielles toutes les informations obtenues dans le cadre de ce contrat. Cela comprend, sans s'y limiter, les informations commerciales, les informations sensibles sur les services et les données personnelles " + ".</p>" +


                "<h3>Article 8 : Signatures</h3>" +

                "<br>"+

                "<p>Fait à Tunis, le <span class='blue-text'>" + LocalDate.now() + "</span> .</p>" +

                "<div class='signatures-container'>" +
                "<div class='signature'>" +
                "<p>La société</p>" +
                "<div class='signature-box'></div>" +
                "</div>" +
                "<div class='signature'>" +
                "<p>Le/La Client(e)</p>" +
                "<div class='signature-box'></div>" +
                "</div>" +
                "</div>" +

                "</div>" +
                "</body></html>";
    }
}
