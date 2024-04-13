package work.project.beercenter.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import work.project.beercenter.model.Client;
import work.project.beercenter.service.UserService;


import java.util.List;

@Component
@PropertySource("classpath:telegram.properties")
public class NotificationService {

    private final UserService userService;
    private final JavaMailSender emailSender;

    @Value("${bot.email.subject}")
    private String emailSubject;

    @Value("${bot.email.from}")
    private String emailFrom;

    @Value("${bot.email.to}")
    private String emailTo;

    public NotificationService(UserService userService, JavaMailSender emailSender) {
        this.userService = userService;
        this.emailSender = emailSender;
    }

    @Scheduled(fixedRate = 100000)
    public void sendNewApplications() {
        List<Client> clients = userService.findNewUsers();
        if (clients.size() == 0)
            return;

        StringBuilder sb = new StringBuilder();

        clients.forEach(user ->
            sb.append("Phone: ")
                    .append(user.getPhone())
                    .append("\r\n")
                    .append("Email: ")
                    .append(user.getEmail())
                    .append("\r\n\r\n")
        );

        sendEmail(sb.toString());
    }

    // c -> smtp ----- smtp -> pop3/imap <--- c

    private void sendEmail(String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailTo);
        message.setFrom(emailFrom);
        message.setSubject(emailSubject);
        message.setText(text);

        emailSender.send(message);
    }
}
