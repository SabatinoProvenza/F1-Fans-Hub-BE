package sabatinoprovenza.F1_Fans_Hub_BE.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.mail.from}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(String toEmail, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Benvenuto nella piattaforma");
        message.setText(
                "Ciao " + username + ",\n\n" +
                        "la tua registrazione è andata a buon fine.\n" +
                        "Benvenuto su F1-Fans-Hub!\n\n" +
                        "A presto!"
        );

        mailSender.send(message);
    }
}