package scratch.BackEnd.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.dto.RequestSurveyDto;
import scratch.BackEnd.service.SurveyResultService;
import scratch.BackEnd.service.SurveyService;
import scratch.BackEnd.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/survey")
@AllArgsConstructor
public class SurveyController {
    private final UserService userService;
    private final SurveyService surveyService;
    private final SurveyResultService surveyResultService;

    @PostMapping("")
    public ResponseEntity<?> createSurvey(@RequestBody RequestSurveyDto requestSurveyDto){
        //System.out.println(requestSurveyDto.toString());
        surveyService.makeSurvey(requestSurveyDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{surveyId}/edit")
    public Long editSurvey(@PathVariable Long surveyId, @RequestBody RequestSurveyDto requestSurveyDto){
        Long newSurveyId = surveyService.editSurvey(surveyId, requestSurveyDto);
        return newSurveyId;
    }



    @GetMapping("/{surveyId}/page")
    public ResponseSurveyDto getSurvey(@PathVariable Long surveyId){
        ResponseSurveyDto responseSurveyDto = surveyService.getSurvey(surveyId);
        System.out.println(responseSurveyDto);
        return responseSurveyDto;
    }

    @GetMapping("")
    public List<SurveyListDto> getSurveyList(HttpServletRequest request){
        User user = userService.getUser(request);
        return surveyService.getSurveyList(user.getKakaoid());
    }

    @DeleteMapping("/{surveyId}")
    public ResponseEntity<?> deleteSurvey(HttpServletRequest request, @PathVariable Long surveyId){
        User user = userService.getUser(request);
        surveyService.deleteSurvey(surveyId, user);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/{surveyId}/early-closing")
    public ResponseEntity<?> closeSurvey(@PathVariable Long surveyId){
        surveyService.closeSurvey(surveyId);
        return ResponseEntity.ok().build();
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

    @PostMapping("/{surveyId}/email-resend")
    public ResponseEntity<?> resendEmail(@PathVariable Long surveyId, @RequestBody RequestResendDto dto){
        surveyService.resendEmail(surveyId, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{surveyId}/email-send")
    public ResponseEntity<?> sendEmail(@PathVariable Long surveyId, @RequestBody RequestSendDto dto){
        surveyService.sendEmail(surveyId, dto);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{surveyId}/result/attend/{attendId}")
    public ResponseUserAnswerTotalDto getUserAnswer(@PathVariable Long surveyId, @PathVariable Long attendId){
        ResponseUserAnswerTotalDto responseUserAnswerTotalDto = surveyResultService.getUserAnswer(surveyId,attendId);

        return responseUserAnswerTotalDto;
    }

    @GetMapping("/{surveyId}/result/user")
    public List<String> getAttendUserList(@PathVariable Long surveyId){
        List<String> userList = new ArrayList<>();
        userList = surveyService.getAttendUserList(surveyId);
        return userList;
    }

    @GetMapping("/{surveyId}/result/attends")
    public ResponseAllAnswerDto getAllAnswer(@PathVariable Long surveyId){
        ResponseAllAnswerDto responseAllAnswerDto = surveyResultService.getAllAnswer(surveyId);
        return responseAllAnswerDto;
    }

    @GetMapping("/{surveyId}/result")
    public ResponseSurveyResultDto getSurveyResult(@PathVariable Long surveyId){
        ResponseSurveyResultDto responseSurveyResultDto = surveyResultService.getSurveyResult(surveyId);
        return responseSurveyResultDto;
    }
}
