package scratch.BackEnd.components;

import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import java.util.HashMap;

@RequiredArgsConstructor
@Component
public class MailUtil {
    private final JavaMailSender javaMailSender;
    private static final String FROM_ADDRESS = "viewer2323@gmail.com";

    private final SpringTemplateEngine templateEngine;

    @Async
    public void bulkSendMail(String[] mails, String subject, String text, HashMap<String, String> inlines){

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(FROM_ADDRESS);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setBcc(mails);
            mimeMessageHelper.setText(text, true);

            if(!inlines.isEmpty()){
                inlines.forEach((key, value) -> {
                    try {
                        mimeMessageHelper.addInline(key, new ClassPathResource(value));
                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
        try {
            javaMailSender.send(msg);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public void sendSurveyInviteMail(HashMap<String, String> values, String[] toList){
        String invitationMail = "invitation-mail";

        Context context = new Context();
        values.forEach(context::setVariable);

        String html = templateEngine.process(invitationMail, context);

        HashMap<String, String> inlineValues = new HashMap<>();

        inlineValues.put("image1", "mail/images/image-1.png");
        inlineValues.put("image2", "mail/images/image-2.png");

        bulkSendMail(toList, "[Kavey] 설문에 응답해주세요!", html, inlineValues);
    }


}
