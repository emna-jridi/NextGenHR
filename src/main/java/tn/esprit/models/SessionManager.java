package tn.esprit.models;

public class SessionManager {


    private static User connectedUser;

    // Méthode pour récupérer l'utilisateur connecté
    public static User getConnectedUser() {
        System.out.println("Récupération de l'utilisateur connecté : " + connectedUser);
        return connectedUser;
    }


    // Méthode pour définir l'utilisateur connecté
    public static void setConnectedUser(User user) {
        connectedUser = user;
    }

    // Méthode pour réinitialiser l'utilisateur connecté (utile lors de la déconnexion)
    public static void clearSession() {
        connectedUser = null;
    }
}
