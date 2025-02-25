package tn.esprit.models;

import java.time.LocalDate;

public class ContratToText {

    public static String generateContract(Contrat contrat) {
        return "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<style>" +
                "body { font-family: Arial, sans-serif; line-height: 1.6; background-color: #f8f9fa; padding: 20px; margin: 0; height: 100vh; display: flex; justify-content: center; align-items: center; }" +
                ".contract-container { max-width: 800px; background: white; padding: 30px; margin: auto; border: 2px solid #ddd; border-radius: 10px; box-shadow: 2px 2px 12px rgba(0, 0, 0, 0.1); position: relative; min-height: 90vh; display: flex; flex-direction: column; justify-content: space-between; }" +
                "h2 { text-align: center; color: #333; }" +
                "h3 { color: #555; border-bottom: 2px solid #ddd; padding-bottom: 5px; }" +
                "p { margin: 10px 0; }" +


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
                "<p><strong>L'Employeur :</strong> NextGenHR, Adresse: 123 Rue de l'Entreprise, Numéro SIRET: 987654321.</p>" +
                "<p><strong>Le Salarié :</strong> " + contrat.getNomClient() + ", Email: " + contrat.getEmailClient() + ".</p>" +
                "<h3>Article 1 : Objet du contrat</h3>" +
                "<p>Le présent contrat a pour objet de définir les conditions de travail entre l'employeur et le salarié. Ce contrat est de type <strong>" + contrat.getTypeContrat() + "</strong>, et entre en vigueur à compter du " + contrat.getDateDebutContrat() + "</strong> à "+ contrat.getDateFinContrat() + ".</p>" +
                "<h3>Article 2 : Durée du travail</h3>" +
                "<p>La durée hebdomadaire du travail est de 40 heures , réparties en 5 jours. Le salarié pourra être amené à effectuer des heures supplémentaires, dans la limite prévue par la législation en vigueur. : " + ".</p>" +
                "<h3>Article 3 : Rémunération</h3>" +
                "<p>Le salarié percevra une rémunération brute mensuelle de <strong>" + contrat.getMontantContrat() + " DT</strong> payable par virement bancaire à la fin de chaque mois.</p>" +
                "<h3>Article 4 : Congés payés</h3>" +
                "<p>Le salarié bénéficie de congés payés annuels selon les dispositions légales en vigueur, soit 15 jours minimum par an. " + ".</p>" +
                "<h3>Article 5 : Confidentialité</h3>" +
                "<p>Le salarié s'engage à respecter la confidentialité des informations relatives à l'entreprise et à ses activités pendant et après la durée du contrat. " + ".</p>" +


                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+
                "<br>"+

                "<h3>Article 6 : Résiliation du contrat</h3>" +
                "<p>En cas de rupture du contrat par l'employeur ou le salarié, un préavis de [durée du préavis] est à respecter, sauf en cas de faute grave. " + ".</p>" +
                "<h3>Article 7 : Divers</h3>" +
                "<p>Les deux parties conviennent de respecter les règles éthiques et professionnelles en vigueur dans l'entreprise. Le salarié déclare avoir pris connaissance des conditions de travail et s'engage à les respecter. " + ".</p>" +






                "<h3>Article 8 : Signatures</h3>" +

                "<br>"+

                "<p>Fait à Tunis, le " + LocalDate.now() + ".</p>" +

                "<div class='signatures-container'>" +
                "<div class='signature'>" +
                "<p>L'Employeur</p>" +
                "<div class='signature-box'></div>" +
                "</div>" +
                "<div class='signature'>" +
                "<p>Le Salarié</p>" +
                "<div class='signature-box'></div>" +
                "</div>" +
                "</div>" +

                "</div>" +
                "</body></html>";
    }
}
