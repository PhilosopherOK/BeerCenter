package work.project.beercenter.mail;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import work.project.beercenter.model.Client;
import work.project.beercenter.service.ClientsService;

@Component
@Data
@PropertySource("classpath:telegram.properties")
public class NotificationService {

    private final ClientsService clientsService;
    private final JavaMailSender emailSender;

    @Value("${bot.email.subject}")
    private String emailSubject;

    @Value("${bot.email.from}")
    private String emailFrom;

    @Value("${bot.email.to}")
    private String emailTo;


    public void sendComplaintsAndWishesToAdminMail(Client client, String theReasonForPetition) {
        StringBuilder sb = new StringBuilder();
        sb.append("Full name: ")
                .append(client.getFullName())
                .append("\r\n")
                .append("Phone: ")
                .append(client.getPhone())
                .append("\r\n")
                .append("Email: ")
                .append(client.getEmail())
                .append("\r\n")
                .append("wanna to say about: " + "\n")
                .append(theReasonForPetition)
                .append("\r\n\r\n");

        sendEmail(sb.toString());
    }

    private void sendEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailTo);
        message.setFrom(emailFrom);
        message.setSubject(emailSubject);
        message.setText(text);

        emailSender.send(message);
    }
}
