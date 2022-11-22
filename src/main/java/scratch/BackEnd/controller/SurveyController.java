package scratch.BackEnd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.service.SurveyService;

import java.util.List;

@RestController
@RequestMapping("/survey")
@AllArgsConstructor
public class SurveyController {
    private final SurveyService surveyService;

    @PostMapping("")
    public String createSurvey(@RequestBody RequestSurveyDto requestSurveyDto){
        //System.out.println(requestSurveyDto.toString());
        surveyService.makeSurvey(requestSurveyDto);
        return "";
    }


    @GetMapping("")
    public List<SurveyListDto> getSurveyList(){
        String email = "asf@asdf.com"; // 인증 추가되면 바뀔 부분
        return surveyService.getSurveyList(email);
    }

    @PostMapping("/{surveyId}/submit")
    public ResponseEntity<?> submitSurvey(@RequestBody RequestSubmitSurveyDto requestSubmitSurveyDto, @PathVariable Long surveyId){
        String email = "asf@asdf.com"; // 인증 정보 오면 바뀔 부분
        surveyService.submitSurvey(requestSubmitSurveyDto, surveyId, email);
        return ResponseEntity.ok().build();
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
