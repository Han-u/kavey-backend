package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import scratch.BackEnd.components.MailUtil;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.repository.*;
import org.springframework.transaction.annotation.Transactional;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.type.SurveyStatus;

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final UserRepository userRepository;
    private final SurveyAttendRepository surveyAttendRepository;
    private final AnswerSubRepository answerSubRepository;
    private final AnswerMultiRepository answerMultiRepository;

    private final MailUtil mailUtil;


    public boolean makeSurvey(RequestSurveyDto requestSurveyDto){
        //설문 생성
        User user = userRepository.findById(requestSurveyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUserId()));
        Survey survey = requestSurveyDto.toEntity(user, SurveyStatus.MAKING);
        Survey surveySaved = surveyRepository.save(survey);

        //질문 생성
        for(int i = 0; i<surveySaved.getQuestionNumber(); i++){
            RequestQuestionDto questionDto = requestSurveyDto.getQuestionList()[i];
            SurveyQuestion question = questionDto.toEntity(surveySaved);
            SurveyQuestion questionSaved = surveyQuestionRepository.save(question);
            //보기 생성
            for (int j = 0; j < questionSaved.getOptionNumber(); j++) {
                QuestionOptionDto questionOptionDto = questionDto.getOptionList()[j];
                QuestionOption questionOption = questionOptionDto.toEntity(questionSaved);
                QuestionOption questionOptionSaved = questionOptionRepository.save(questionOption);
            }

        }

        return true;
    }
    public List<SurveyListDto> getSurveyList(String email){
        User user = userRepository.findByEmail(email);
        List<Survey> surveyList =  surveyRepository.findByUser(user);
        return surveyList.stream().map(SurveyListDto::fromEntity).collect(Collectors.toList());
    }

    public void deleteSurvey(Long surveyId){
        // 설문지 조회
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        // 해당 유저에게 권한이 있는지 확인
        // 카카오 인증 추가되면 수정함
//        User user = survey.getUser();
//        if (!Objects.equals(user.getEmail(), "asf@asdf.com")){
//            throw new RuntimeException("권한이 없습니다.");
//        }

        // 설문하는 동안에는 삭제 못하도록함
        if (survey.getStatus() == SurveyStatus.PROGRESS){
            throw new RuntimeException("진행중인 설문은 삭제가 불가능합니다.");
        }

        surveyRepository.deleteById(surveyId);
    }

    public void closeSurvey(Long surveyId){
        // 설문지 조회
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        // 해당 유저에게 권한이 있는지 확인

        // 설문 진행중인 설문만 조기종료 가능
        if (survey.getStatus() != SurveyStatus.PROGRESS){
            throw new RuntimeException("진행중인 설문만 조기종료가 가능합니다.");
        }

        // 이미 조기 종료된 설문도 조기종료 불가능
        if (survey.getEarlyEndDate() != null){
            throw new RuntimeException("이미 종료된 설문입니다.");
        }

        survey.setEarlyEndDate(LocalDateTime.now());
        survey.setStatus(SurveyStatus.DONE);
        surveyRepository.save(survey);
    }

    /**
     * 설문 참여자가 제출한 설문 응답을 저장한다.
     */
    @Transactional
    public void submitSurvey(RequestSubmitSurveyDto requestSubmitSurveyDto, Long surveyId, String userEmail) {
        // 1. 해당 설문지 찾기
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));
        User user = userRepository.findByEmail(userEmail);
        isValidSurvey(survey);
        hasPermission(survey, userEmail);

        // 2. 각 질문별 저장하기
        // 2-1. 주관식 저장하기
        List<AnswerSub> answerSubList = requestSubmitSurveyDto.getSurveySubjective().stream()
                        .map(e -> {
                            SurveyQuestion surveyQuestion = surveyQuestionRepository.findByQuestionIdAndSurvey(e.getQuestionId(), survey).orElseThrow(() -> new RuntimeException("BAD REQUEST"));
                            return new AnswerSub(surveyQuestion, user, e.getValue());
                        })
                        .collect(Collectors.toList());
        answerSubRepository.saveAll(answerSubList);

        // 2-2. 객관식 저장하기
        List<AnswerMulti> answerMultiList = requestSubmitSurveyDto.getSurveyMultiple().stream()
                .map(e -> {
                    SurveyQuestion surveyQuestion = surveyQuestionRepository.findByQuestionIdAndSurvey(e.getQuestionId(), survey).orElseThrow(() -> new RuntimeException("BAD REQUEST2"));
                    QuestionOption questionOption = questionOptionRepository.findByOptionIdAndSurveyQuestion(e.getOptionId(), surveyQuestion).orElseThrow(() -> new RuntimeException("BAD REQUEST3"));
                    return new AnswerMulti(surveyQuestion, user, questionOption);
                })
                .collect(Collectors.toList());
        answerMultiRepository.saveAll(answerMultiList);

        // 3. attend 테이블 업데이트하기
        Optional<SurveyAttend> surveyAttend = surveyAttendRepository.findBySurveyAndSendEmail(survey, userEmail);

        if(surveyAttend.isPresent()) {
            SurveyAttend userAttendInfo = surveyAttend.get();
            userAttendInfo.setAge(requestSubmitSurveyDto.getAge());
            userAttendInfo.setGender(requestSubmitSurveyDto.getGender());
            userAttendInfo.setStatus(AttendStatus.RESPONSE);
            userAttendInfo.setResponseDate(LocalDateTime.now());
            surveyAttendRepository.save(userAttendInfo);
        }else{
            surveyAttendRepository.save(
                    SurveyAttend.builder()
                            .age(requestSubmitSurveyDto.getAge())
                            .gender(requestSubmitSurveyDto.getGender())
                            .sendEmail(userEmail)
                            .status(AttendStatus.RESPONSE)
                            .survey(survey)
                            .responseDate(LocalDateTime.now())
                            .build()
            );
        }

    }

    void isValidSurvey(Survey survey) {
        // 1-1. 해당 설문에 응답 가능한 날짜인지
        LocalDateTime startDate = survey.getSurveyStartDate();
        LocalDateTime endDate = survey.getSurveyEndDate();
        LocalDateTime now = LocalDateTime.now();

        if(!isBetweenDay(now, startDate, endDate)){
            throw new RuntimeException("응답 가능한 날짜가 아닙니다.");
        }

        // 1-2. 해당 설문이 응답 가능한 상태인지
        SurveyStatus status = survey.getStatus();
        if (status != SurveyStatus.PROGRESS){
            throw new RuntimeException("응답 가능한 설문이 아닙니다.");
        }

    }
    void hasPermission(Survey survey, String email){
        Optional<SurveyAttend> surveyAttend = surveyAttendRepository.findBySurveyAndSendEmail(survey, email);

        if (survey.isPrivate()){
            // 1. 폐쇄형인 경우
            // 1-1. 해당 설문에 대한 참여 권한이 있는지
            if (surveyAttend.isPresent()){
                if (surveyAttend.get().getStatus() != AttendStatus.NONRESPONSE){
                    throw new RuntimeException("응답할 수 없는 설문입니다.");
                }
            }else{
                throw new RuntimeException("참여권한이 없습니다.");
            }

        }
        else{
            // 2. 오픈형인 경우
            // 2-1. 참여 제한에 걸려있는지?
            int participants = surveyAttendRepository.countBySurveyAndStatus(survey, AttendStatus.RESPONSE);
            if (survey.getLimitPerson() <= participants){
                throw new RuntimeException("선착순 끝~ 더빨리 움직이셈ㅋㅋ");
            }
        }
    }

    private Boolean isBetweenDay(LocalDateTime targetDate, LocalDateTime from, LocalDateTime to){
        return (targetDate.isAfter(from) || targetDate.isEqual(from)) && (targetDate.isBefore(to) || targetDate.isEqual(to));
    }
    public List<SurveyReceiverDto> getSurveyReceiver(Long surveyId) {
        List<SurveyAttend> attends = surveyAttendRepository.findBySurvey_SurveyId(surveyId);
        return attends.stream().map(SurveyReceiverDto::fromEntity).collect(Collectors.toList());
    }

    public void addSurveyReceiver(Long surveyId, RequestAddSurveyReceiverDto dto) {
        // 해당 설문이 있는지 확인
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        // 이미 추가된 이메일인지 확인
        Optional<SurveyAttend> attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, dto.getEmail());
        if (attend.isPresent()) {
            throw new RuntimeException("이미 추가된 이메일 입니다.");
        }

        // 이메일 전송하는 부분 나중에 수정
        String email = dto.getEmail();
        String subject = "설문에 참여해주세요!";
        String text = "<p>다음 설문에 응답해주세요!</p> <p>링크클릭</p>" +
                "<div><a href='#'>설문 응답하러가기</a></div>";
        mailUtil.sendMail(email, subject, text);


        // 추가함
        surveyAttendRepository.save(SurveyAttend.builder()
                .sendDate(LocalDateTime.now())
                .sendEmail(dto.getEmail())
                .status(AttendStatus.NONRESPONSE)
                .survey(survey)
                .build());
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
