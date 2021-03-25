package znbang.svn.common;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class Mailer {
    private JavaMailSender mailSender;

    public Mailer(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(String sender, String recipient, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(sender);
        msg.setTo(recipient);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }
}
