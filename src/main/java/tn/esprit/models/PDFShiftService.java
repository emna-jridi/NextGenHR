package tn.esprit.models;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.Base64;
import org.json.JSONObject;
import java.io.IOException;

public class PDFShiftService {

    private static final String API_KEY = "sk_66b7eca7405741e477d2af2c039d781f6cdc3c86"; // Remplace par ta clé API

    // Méthode pour générer le PDF
    public static void generatePDF(String htmlContent, String outputFilePath) throws IOException {
        // Encoder l'API Key en Base64
        String encodedApiKey = Base64.getEncoder().encodeToString((API_KEY + ":").getBytes(StandardCharsets.UTF_8));

        // Créer le JSON pour envoyer à l'API PDFShift
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("source", htmlContent);

        // Créer la requête HTTP pour l'API PDFShift
        var httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.pdfshift.io/v3/convert/pdf"))
                .timeout(Duration.ofSeconds(20))
                .header("Content-Type", "application/json")
                .header("Authentication", "Basic " + encodedApiKey) // Authentification avec la clé API
                .POST(HttpRequest.BodyPublishers.ofString(jsonObject.toString()))
                .build();

        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();

        try {
            // Envoyer la requête et récupérer la réponse
            var response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

            var statusCode = response.statusCode();
            if (statusCode == 200 || statusCode == 201) {
                // Sauvegarder le fichier PDF localement
                var targetFile = new File(outputFilePath);
                Files.copy(response.body(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } else {
                throw new IOException("Erreur PDFShift API: " + statusCode);
            }
        } catch (InterruptedException e) {
            // Gérer l'exception proprement
            Thread.currentThread().interrupt();
            throw new IOException("La requête a été interrompue", e);
        }
    }
}
