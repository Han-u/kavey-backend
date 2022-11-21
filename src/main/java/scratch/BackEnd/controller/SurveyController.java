package scratch.BackEnd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyAttend;
import scratch.BackEnd.dto.RequestAddSurveyReceiverDto;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.dto.SurveyReceiverDto;
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
        //System.out.println(requestSurveyDto.toString());
        surveyService.makeSurvey(requestSurveyDto);
        return "";
    }


    @GetMapping("")
    public List<Survey> getSurveyList(){
        return surveyService.readSurvey();
    }

    @GetMapping("/{surveyId}/receiver")
    public ResponseEntity<?> getSurveyReceiver(@PathVariable Long surveyId){
        List<SurveyReceiverDto> list = surveyService.getSurveyReceiver(surveyId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/{surveyId}/receiver")
    public ResponseEntity<?> addSurveyReceiver(@PathVariable Long surveyId, @RequestBody RequestAddSurveyReceiverDto dto){
        surveyService.addSurveyReceiver(surveyId, dto);
        return ResponseEntity.ok().build();
    }
}
