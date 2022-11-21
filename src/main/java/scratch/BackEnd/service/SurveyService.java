package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.components.MailUtil;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserRepository userRepository;

    private final SurveyAttendRepository surveyAttendRepository;

    private final MailUtil mailUtil;


    public boolean makeSurvey(RequestSurveyDto requestSurveyDto){

        //System.out.println(requestSurveyDto.toString());      //질문 is_requuired 자꾸 false로 나오는 이유 찾아내기
        User user = userRepository.findById(requestSurveyDto.getUser_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUser_id()));;
        Survey survey = requestSurveyDto.toEntity(user);
        Survey surveySaved = surveyRepository.save(survey);
        //System.out.println(surveySaved.toString());

        System.out.println(survey.getQuestionNumber());
        for(int i = 0; i<surveySaved.getQuestionNumber(); i++){
            RequestQuestionDto questionDto = requestSurveyDto.getQuestion_list()[i];
            SurveyQuestion question = questionDto.toEntity(surveySaved);
            //System.out.println(question.toString());
            SurveyQuestion questionSaved = surveyQuestionRepository.save(question);
            for (int j = 0; j < questionSaved.getOptionNumber(); j++) {
                QuestionOptionDto questionOptionDto = new QuestionOptionDto(null,questionDto.getOption_list()[j],j+1);
                //System.out.println(questionOptionDto.toString());
                QuestionOption questionOption = questionOptionDto.toEntity(questionSaved);
                QuestionOption questionOptionSaved = questionOptionRepository.save(questionOption);
            }

        }


        return true;
    }
    public List<Survey> readSurvey(){
        return surveyRepository.findAll();
    }

    public List<SurveyReceiverDto> getSurveyReceiver(Long surveyId) {
        List<SurveyAttend> attends = surveyAttendRepository.findBySurvey_SurveyId(surveyId);
        return attends.stream().map(SurveyReceiverDto::fromEntity).collect(Collectors.toList());
    }

    public void addSurveyReceiver(Long surveyId, RequestAddSurveyReceiverDto dto) {
        // 해당 설문이 있는지 확인
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        // 이미 추가된 이메일인지 확인
        Optional<SurveyAttend> attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, dto.getEmail());
        if (attend.isPresent()) {
            throw new RuntimeException("이미 추가된 이메일 입니다.");
        }

        // 이메일 전송하는 부분 나중에 수정
        String email = dto.getEmail();
        String subject = "설문에 참여해주세요!";
        String text = "<p>다음 설문에 응답해주세요!</p> <p>링크클릭</p>" +
                "<div><a href='#'>설문 응답하러가기</a></div>";
        mailUtil.sendMail(email, subject, text);


        // 추가함
        surveyAttendRepository.save(SurveyAttend.builder()
                .sendDate(LocalDateTime.now())
                .sendEmail(dto.getEmail())
                .status(AttendStatus.NONRESPONSE)
                .survey(survey)
                .build());
    }

}
