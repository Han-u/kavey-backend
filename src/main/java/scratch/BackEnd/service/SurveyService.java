package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.QuestionOption;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.dto.QuestionOptionDto;
import scratch.BackEnd.dto.RequestQuestionDto;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.repository.QuestionOptionRepository;
import scratch.BackEnd.repository.SurveyQuestionRepository;
import scratch.BackEnd.repository.SurveyRepository;
import scratch.BackEnd.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserRepository userRepository;


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

}
