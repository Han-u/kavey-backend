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
    public static final String FRONT_URL = "http://localhost:3000";


    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendMail(String mail, String subject, String text, HashMap<String, String> inlines){

        MimeMessagePreparator msg = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            mimeMessageHelper.setTo(mail);
            mimeMessageHelper.setSubject(subject);
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
    public void sendSurveyInviteMail(HashMap<String, String> values, String[] toList, Long surveyId){
        String invitationMail = "invitation-mail";

        HashMap<String, String> inlineValues = new HashMap<>();

        inlineValues.put("image1", "mail/images/image-1.png");
        inlineValues.put("image2", "mail/images/image-2.png");

        for (String email: toList) {
            Context context = new Context();
            values.forEach(context::setVariable);
            context.setVariable("surveyLink", FRONT_URL+"/answer/"+surveyId);
            context.setVariable("rejectLink", FRONT_URL+"/reject?surveyId="+surveyId+"&email="+email);

            String html = templateEngine.process(invitationMail, context);
            sendMail(email, "[Kavey] 설문에 응답해주세요!", html, inlineValues);
        }

    }


}
