package tn.esprit.services;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class ServiceMail {
    private final String username = "myriambenabdallah17@gmail.com";
    private final String password = "cuzp ccxe deai bxrg";

    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + to);
        } catch (MessagingException e) {
            System.err.println("Erreur d'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'envoi de l'email", e);
        }
    }

}
