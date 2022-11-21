package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.QuestionOptionDto;
import scratch.BackEnd.dto.RequestQuestionDto;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.repository.*;

import java.util.List;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionTypeRepository questionTypeRepository;
    private final UserRepository userRepository;


    public boolean makeSurvey(RequestSurveyDto requestSurveyDto){
        //설문 생성
        User user = userRepository.findById(requestSurveyDto.getUser_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUser_id()));
        Survey survey = requestSurveyDto.toEntity(user, SurveyStatus.MAKING);
        Survey surveySaved = surveyRepository.save(survey);
        //System.out.println(surveySaved.toString());

        //질문 생성
        for(int i = 0; i<surveySaved.getQuestionNumber(); i++){
            RequestQuestionDto questionDto = requestSurveyDto.getQuestion_list()[i];
            QuestionType type = questionTypeRepository.findById(questionDto.getType()).orElseThrow(() -> new IllegalArgumentException("해당 질문타입은 없습니다. id = " + questionDto.getType()));
            SurveyQuestion question = questionDto.toEntity(surveySaved, type);
            SurveyQuestion questionSaved = surveyQuestionRepository.save(question);
            //보기 생성
            for (int j = 0; j < questionSaved.getOptionNumber(); j++) {
                QuestionOptionDto questionOptionDto = questionDto.getOption_list()[j];
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
