package site.ogobi.ogobi.boundedContext.auth.service;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import site.ogobi.ogobi.boundedContext.member.entity.Member;
import site.ogobi.ogobi.boundedContext.member.service.MemberService;

import java.util.Properties;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final Environment environment;
    private final MemberService memberService;

    @Transactional
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
        message.setSubject("비밀번호 초기화 인증번호 입니다.");
        message.setText("비밀번호 초기화 인증번호: " + resetToken);

        // Send the email
        Transport.send(message);
    }

    public String generateResetToken(int length) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, length);
    }

    @Transactional
    public void setMemberResetToken(String resetToken, String email) {
        Member foundMember = memberService.findByEmail(email);
        if (foundMember == null) {
            return;
        }
        foundMember.setResetToken(resetToken);
    }



}
