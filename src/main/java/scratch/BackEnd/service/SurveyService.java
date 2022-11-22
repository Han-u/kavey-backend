package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.repository.*;

import java.util.ArrayList;
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
    public ResponseSurveyDto getSurvey(Long surveyId){
        System.out.println("설문 찾기 id : " + surveyId);
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문은 없습니다. id = " + surveyId));


        List<SurveyQuestion> surveyQuestions = surveyQuestionRepository.findBySurvey(survey);       //설문 찾기

        List<ResponseQuestionDto> responseQuestionDtos = new ArrayList<>();                         //설문에 포함된 질문 리스트로 만들기
        for (SurveyQuestion surveyQuestion : surveyQuestions) {
            List <QuestionOption> questionQuestions = questionOptionRepository.findBySurveyQuestion(surveyQuestion);

            List <ResponseQuestionOptionDto> responseQuestionOptionDtos = new ArrayList<>();        //질문 보기 리스트로 만들기
            for (QuestionOption questionOption : questionQuestions) {
                responseQuestionOptionDtos.add(new ResponseQuestionOptionDto(questionOption));
            }
            responseQuestionDtos.add(new ResponseQuestionDto(surveyQuestion, responseQuestionOptionDtos));
        }
        ResponseSurveyDto responseSurveyDto = new ResponseSurveyDto(survey, responseQuestionDtos);

        return responseSurveyDto;
    }

    public List<Survey> readSurvey(){
        return surveyRepository.findAll();
    }

}
