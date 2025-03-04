package tn.esprit.models;
import java.time.LocalDate;

public class Reunion {


        private int id;
        private String titre;
        private String description;
        private LocalDate date;
        private String type; // "en ligne" ou "présentiel"

        // Constructeur par défaut
        public Reunion() {
        }

        // Constructeur avec tous les attributs
        public Reunion(int id, String titre, String description, LocalDate date, String type) {
            this.id = id;
            this.titre = titre;
            this.description = description;
            this.date = date;
            this.type = type;
        }

        // Constructeur sans id (pour les nouvelles réunions)
        public Reunion(String titre, String description, LocalDate date, String type) {
            this.titre = titre;
            this.description = description;
            this.date = date;
            this.type = type;
        }

        // Getters et Setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitre() {
            return titre;
        }

        public void setTitre(String titre) {
            this.titre = titre;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        // Méthode toString
        @Override
        public String toString() {
            return "Reunion{" +
                    "id=" + id +
                    ", titre='" + titre + '\'' +
                    ", description='" + description + '\'' +
                    ", date=" + date +
                    ", type='" + type + '\'' +
                    '}';
        }
    }


