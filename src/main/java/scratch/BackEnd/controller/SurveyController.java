package scratch.BackEnd.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.service.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/survey")
@AllArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @GetMapping("")
    public List<Survey> getSurveyList(){
        return surveyService.readSurvey();
    }
}
