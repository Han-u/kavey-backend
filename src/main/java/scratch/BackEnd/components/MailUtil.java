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
    private static final String FROM_ADDRESS = "viewer2323@gamil.com";

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

    @Async
    public void bulkSendMail(String[] mails, String subject, String text){

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(FROM_ADDRESS);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setBcc(mails);
            mimeMessageHelper.setText(text, true);
        };
        try {
            javaMailSender.send(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
