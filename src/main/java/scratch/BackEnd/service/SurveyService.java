package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public List<Survey> readSurvey(){
        return surveyRepository.findAll();
    }

}
