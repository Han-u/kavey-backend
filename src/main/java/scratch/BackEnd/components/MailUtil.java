package scratch.BackEnd.components;

import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailUtil {
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(String mail, String subject, String text){

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(mail);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, true);
        };
        try {
            javaMailSender.send(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
