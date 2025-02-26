package tn.esprit.services;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class OpenAIService {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static void main(String[] args) {
        try {
            String response = sendOpenAIRequest("Write a haiku about AI");
            System.out.println("Response: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String sendOpenAIRequest(String prompt) throws Exception {
        String requestBody = "{"
                + "\"model\": \"gpt-4o-mini\","
                + "\"store\": true,"
                + "\"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]"
                + "}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}
