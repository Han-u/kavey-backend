package scratch.BackEnd.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.service.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/survey")
@AllArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping("")
    public String createSurvey(@RequestBody RequestSurveyDto requestSurveyDto){
        //ObjectMapper objectMapper = new ObjectMapper();   //카멜 스네이크 바꾸는거 하기
        System.out.println(requestSurveyDto.toString());
        surveyService.makeSurvey(requestSurveyDto);
        return "";
    }


    @GetMapping("")
    public List<Survey> getSurveyList(){
        return surveyService.readSurvey();
    }

}
