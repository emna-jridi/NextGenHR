package controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

public class GoogleCalendarService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);

    // Le fichier de credentials doit se trouver dans le classpath (ex. dans src/main/resources)
    private static final String CREDENTIALS_RESOURCE = "client_secret_1070775264610-pkfrc6osrdnhjmbjbg4foghbjkm96i7r.apps.googleusercontent.com.json";

    private Calendar service;

    public GoogleCalendarService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        this.service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("Google Calendar API Example")
                .build();
    }

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Charger le fichier de credentials depuis le classpath
        InputStream in = GoogleCalendarService.class.getClassLoader().getResourceAsStream(CREDENTIALS_RESOURCE);
        if (in == null) {
            throw new IOException("Resource not found: " + CREDENTIALS_RESOURCE);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void createEvent(String summary, String description, Instant startTime, Instant endTime) throws IOException {
        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        // Formatage des dates en ISO_INSTANT et configuration du fuseau horaire en UTC
        event.setStart(new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        DateTimeFormatter.ISO_INSTANT.format(startTime.atOffset(ZoneOffset.UTC))))
                .setTimeZone("UTC"));
        event.setEnd(new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(
                        DateTimeFormatter.ISO_INSTANT.format(endTime.atOffset(ZoneOffset.UTC))))
                .setTimeZone("UTC"));

        event = service.events().insert("primary", event).execute();
        System.out.println("Événement créé : " + event.getHtmlLink());
    }
}