package site.ogobi.ogobi.boundedContext.auth.service;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class EmailService {

    @Autowired
    private Environment environment;

    public void sendResetTokenEmail(String recipientEmail, String resetToken) throws MessagingException {
        // Configure the email properties
        Properties properties = new Properties();
        String host = environment.getProperty("spring.mail.host");
        String port = environment.getProperty("spring.mail.port");
        String username = environment.getProperty("spring.mail.username");
        String password = environment.getProperty("spring.mail.password");

        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Create the session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        // Create the email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
        message.setSubject("Password Reset");
        message.setText("Your password reset token is: " + resetToken);

        // Send the email
        Transport.send(message);
    }
}
