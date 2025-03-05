package tn.esprit.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.client.util.DateTime;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Date;

public class GoogleCalendarService {

    private static final String APPLICATION_NAME = "Ressources Humaines";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "src/main/java/tn/esprit/utils/credentials.json";

    // Créez un service Google Calendar authentifié
    public static Calendar getCalendarService() throws IOException, GeneralSecurityException {
        // Chargez les informations d'identification depuis le fichier JSON
        File credentialsFile = new File(CREDENTIALS_FILE_PATH);
        if (!credentialsFile.exists()) {
            System.out.println("Le fichier credentials.json n'a pas été trouvé.");
            return null;
        } else {
            System.out.println("Le fichier credentials.json a été trouvé.");
        }

        FileInputStream credentialsStream = new FileInputStream(credentialsFile);

        // Créez les informations d'identification OAuth2
        Credential credential = GoogleCredential.fromStream(credentialsStream)
                .createScoped(Collections.singleton(CalendarScopes.CALENDAR));

        // Créez une instance du service Google Calendar
        return new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
    public static void addEvent() throws IOException, GeneralSecurityException {
        Calendar service = getCalendarService(); // Récupérer le service Google Calendar

        // Vérifier si le service est bien récupéré
        if (service == null) {
            System.out.println("Le service Google Calendar n'a pas pu être initialisé.");
            return;
        }

        try {
            // Créez un événement
            Event event = new Event()
                    .setSummary("Réunion importante")
                    .setLocation("Chaouach")
                    .setDescription("Réunion pour discuter des objectifs du projet.");

            // Définir la date de début de l'événement
            Date startDate = new Date(); // Utilisez une date appropriée
            EventDateTime start = new EventDateTime()
                    .setDateTime(new DateTime(startDate))
                    .setTimeZone("Africa/Tunis"); // Choisissez le fuseau horaire approprié
            event.setStart(start);

            // Définir la date de fin de l'événement
            Date endDate = new Date(startDate.getTime() + 60 * 60 * 1000); // Une heure après le début
            EventDateTime end = new EventDateTime()
                    .setDateTime(new DateTime(endDate))
                    .setTimeZone("Africa/Tunis");
            event.setEnd(end);

            // Ajoutez l'événement à votre calendrier
            String calendarId = "primary"; // Utilisez "primary" ou l'ID d'un autre calendrier si nécessaire
            event = service.events().insert(calendarId, event).execute();

            // Affichez l'URL de l'événement
            System.out.println("Événement ajouté: " + event.getHtmlLink());

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'ajout de l'événement.");
        }
    }
}
