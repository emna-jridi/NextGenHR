package tn.esprit.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import tn.esprit.models.Questions;
import tn.esprit.models.TestResult;

public class QuizApiService {

    private final String API_KEY;
    private final String BASE_URL = "https://quizapi.io/api/v1/questions";
    private Map<Integer, List<String>> correctAnswersMap = new HashMap<>();

    public QuizApiService() {
        this.API_KEY = "GkB1Q7OuvJEbtlJFjcNBBD8tfgNi3tIa70zFvbX5";
    }

    public QuizApiService(String apiKey) {
        this.API_KEY = apiKey;
    }

    /**
     * Récupérer des questions depuis l'API
     */
    public List<Questions> fetchQuestions(String category, int limit) throws Exception {
        // Vider le stockage temporaire des réponses correctes
        correctAnswersMap.clear();

        // Construction de l'URL avec les paramètres
        String urlStr = BASE_URL + "?category=" + category + "&limit=" + limit;
        URL url = new URL(urlStr);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        // Ajout de l'en-tête d'API key (important pour QuizAPI)
        conn.setRequestProperty("X-Api-Key", API_KEY);

        // Lecture de la réponse
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // Parser la réponse JSON
        JSONArray questionsArray = new JSONArray(response.toString());
        List<Questions> questions = new ArrayList<>();

        for (int i = 0; i < questionsArray.length(); i++) {
            JSONObject questionObj = questionsArray.getJSONObject(i);
            Questions question = new Questions();

            // Certaines questions n'ont pas d'ID, utiliser i+1 comme fallback
            int questionId = questionObj.has("id") ? questionObj.getInt("id") : i + 1;
            question.setId(questionId);
            question.setQuestion(questionObj.getString("question"));

            // Parser les réponses possibles
            JSONObject answers = questionObj.getJSONObject("answers");
            List<String> answerList = new ArrayList<>();

            for (String key : answers.keySet()) {
                if (!answers.isNull(key)) {
                    answerList.add(answers.getString(key));
                }
            }
            question.setAnswers(answerList);

            // Parser les réponses correctes et stocker les clés dans notre map temporaire
            JSONObject correctAnswers = questionObj.getJSONObject("correct_answers");
            List<String> correctAnswerKeys = new ArrayList<>();

            for (String key : correctAnswers.keySet()) {
                if (correctAnswers.getBoolean(key)) {
                    correctAnswerKeys.add(key.replace("_correct", ""));
                    // Définir la première réponse correcte trouvée
                    if (question.getCorrectAnswer() == null) {
                        question.setCorrectAnswer(key.replace("_correct", ""));
                    }
                }
            }

            // Stocker les clés des réponses correctes dans notre map temporaire
            correctAnswersMap.put(questionId, correctAnswerKeys);

            // Debug: Afficher les réponses correctes
            System.out.println("Question ID: " + questionId + " - Réponses correctes: " + correctAnswerKeys);

            questions.add(question);
        }

        return questions;
    }

    public TestResult checkAnswers(List<Questions> questions, Map<Integer, String> userAnswers, int employeeId, String testCategory) {
        int totalQuestions = questions.size();
        int correctAnswers = 0;
        StringBuilder analysisNotes = new StringBuilder("Analyse détaillée des réponses:\n");

        for (Questions question : questions) {
            int questionId = question.getId();
            String userAnswer = userAnswers.get(questionId);

            // Si l'utilisateur a répondu à cette question
            if (userAnswer != null) {
                List<String> correctAnswerKeys = correctAnswersMap.get(questionId);
                boolean isCorrect = false;

                // Debug: Afficher la réponse de l'utilisateur et les bonnes réponses
                System.out.println("Réponse utilisateur pour la question " + questionId + ": " + userAnswer);
                System.out.println("Réponses correctes: " + correctAnswerKeys);

                // Vérifier si la réponse de l'utilisateur est correcte
                if (correctAnswerKeys != null) {
                    for (String correctKey : correctAnswerKeys) {
                        if (userAnswer.equals(correctKey)) {
                            isCorrect = true;
                            break;
                        }
                    }
                }

                if (isCorrect) {
                    correctAnswers++;
                    analysisNotes.append("Question ").append(questionId).append(": Correcte\n");
                } else {
                    analysisNotes.append("Question ").append(questionId).append(": Incorrecte. ");
                    analysisNotes.append("Votre réponse: ").append(userAnswer);
                    analysisNotes.append(", Réponse correcte: ").append(question.getCorrectAnswer()).append("\n");
                }
            } else {
                analysisNotes.append("Question ").append(questionId).append(": Non répondue\n");
            }
        }

        // Calculer le score en pourcentage
        double score = totalQuestions > 0 ? (double) correctAnswers / totalQuestions * 100 : 0;
        System.out.println("Total correct answers: " + correctAnswers);

        // Créer l'objet TestResult
        TestResult result = new TestResult();
        result.setEmployeeId(employeeId);
        result.setTestCategory(testCategory);
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        result.setScore(score);
        result.setAnalysisNotes(analysisNotes.toString());

        return result;
    }
}
