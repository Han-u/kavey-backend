package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.repository.SurveyRepository;
import scratch.BackEnd.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    public boolean makeSurvey(RequestSurveyDto requestSurveyDto){

        User user = userRepository.findById(requestSurveyDto.getUser_id()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUser_id()));;
        Survey survey = requestSurveyDto.toEntity(user);
        Survey surveySaved = surveyRepository.save(survey);
        //System.out.println(surveySaved.toString());



        return true;
    }
    public List<Survey> readSurvey(){
        return surveyRepository.findAll();
    }

}
