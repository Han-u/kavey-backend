package scratch.BackEnd.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scratch.BackEnd.components.MailUtil;
import scratch.BackEnd.domain.*;
import scratch.BackEnd.dto.*;
import scratch.BackEnd.exception.CustomException;
import scratch.BackEnd.exception.ErrorCode;
import scratch.BackEnd.repository.*;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.type.SurveyStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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


    private final SurveyScheduleRepository surveyScheduleRepository;

    public void addSurveySchedule(Survey survey){
        SurveySchedule surveyStartSchedule = new SurveySchedule(survey,survey.getSurveyStartDate(), SurveyStatus.PROGRESS);
        SurveySchedule surveyEndSchedule = new SurveySchedule(survey,survey.getSurveyEndDate(), SurveyStatus.DONE);
        surveyScheduleRepository.save(surveyStartSchedule);
        surveyScheduleRepository.save(surveyEndSchedule);
    }

    public void removeSurveySchedule(Survey survey){
        surveyScheduleRepository.deleteAllBySurvey(survey);
    }
    public void updateSurveySchedule(Survey survey){
        this.removeSurveySchedule(survey);
        this.addSurveySchedule(survey);
    }

    public Long makeSurvey(RequestSurveyDto requestSurveyDto){
        //설문 생성
        User user = userRepository.findById(requestSurveyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUserId()));
        Survey survey = requestSurveyDto.toEntity(user, SurveyStatus.MAKING);

        Survey surveySaved = surveyRepository.save(survey);
        makeQuestion(surveySaved, requestSurveyDto.getQuestionList());

        addSurveySchedule(surveySaved); //스케줄에 추가

        return surveySaved.getSurveyId();
    }

    private void makeQuestion(Survey surveySaved, RequestQuestionDto[] questionDtos) {
        //질문 생성
        for(RequestQuestionDto questionDto: questionDtos){
            SurveyQuestion question = questionDto.toEntity(surveySaved);
            SurveyQuestion questionSaved = surveyQuestionRepository.save(question);
            //보기 생성
            for (int j = 0; j < questionSaved.getOptionNumber(); j++) {
                QuestionOptionDto questionOptionDto = questionDto.getOptionList()[j];
                QuestionOption questionOption = questionOptionDto.toEntity(questionSaved);
                QuestionOption questionOptionSaved = questionOptionRepository.save(questionOption);
            }
        }
    }


    public Long editSurvey(Long surveyId, RequestSurveyDto requestSurveyDto){
        System.out.println("설문 수정 : " + surveyId);
        //권환 확인 추가하기
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));
        // 설문하는 동안에는 수정 못하도록함
        if (survey.getStatus() == SurveyStatus.PROGRESS){
            throw new RuntimeException("진행중인 설문은 수정이 불가능합니다.");
        }

        User user = userRepository.findById(requestSurveyDto.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당 유저는 없습니다. id = " + requestSurveyDto.getUserId()));


//        surveyQuestionRepository.deleteBySurvey(survey);

//        for(SurveyQuestion surveyQuestion : survey.getSurveyQuestions()) {
//            surveyQuestionRepository.deleteById(surveyQuestion.getQuestionId());
//        }

        List<SurveyQuestion> toBeRemoved = survey.getSurveyQuestions();
        survey.getSurveyQuestions().removeAll(toBeRemoved);


        survey.update(requestSurveyDto.toEntity(user, survey.getStatus()));
        Survey surveySaved = surveyRepository.save(survey);
        makeQuestion(survey, requestSurveyDto.getQuestionList());

        updateSurveySchedule(surveySaved);
        return surveySaved.getSurveyId();
    }


    @Transactional
    public List<SurveyListDto> getSurveyList(Long kakaoid){
        List<Survey> surveyList = surveyRepository.findByUserid(kakaoid);
        List<SurveyListDto> dto =surveyList.stream().map(survey -> {
            int participants = surveyAttendRepository.countBySurveyAndStatus(survey, AttendStatus.RESPONSE);
            return new SurveyListDto(survey, participants);
        }).collect(Collectors.toList());
        return dto;
    }

    public void deleteSurvey(Long surveyId, User user){
        // 설문지 조회
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
//         해당 유저에게 권한이 있는지 확인
        User surveyCreator = survey.getUser();
        if (!Objects.equals(surveyCreator.getEmail(), user.getEmail())){
            throw new CustomException(ErrorCode.DOES_NOT_HAVE_PERMISSION);
        }

        // 설문하는 동안에는 삭제 못하도록함
        if (survey.getStatus() == SurveyStatus.PROGRESS){
            throw new CustomException(ErrorCode.PROGRESS_CANNOT_BE_MODIFIED);
        }

        removeSurveySchedule(survey);       //스케줄에서 삭제
        surveyRepository.deleteById(surveyId);
    }



    public void startSurvey(Long surveyId){ //설문 시작
        System.out.println("설문 시작 : " + surveyId);
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        survey.setStatus(SurveyStatus.PROGRESS);    //상태 바꾸기

        //필요한기능 있으면 넣기

        //설문 시작시 제작자한테 알림 메일보내기라든가...

        surveyRepository.save(survey);
    }
    public void closeSurvey(Long surveyId){
        // 설문지 조회
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        // 해당 유저에게 권한이 있는지 확인

        // 설문 진행중인 설문만 조기종료 가능
        if (survey.getStatus() != SurveyStatus.PROGRESS){
            throw new CustomException(ErrorCode.ONLY_PROGRESS_CAN_EARLY_CLOSE);
        }

        // 이미 조기 종료된 설문도 조기종료 불가능
        if (survey.getEarlyEndDate() != null){
            throw new CustomException(ErrorCode.SURVEY_ALREADY_BEEN_COMPLETED);
        }

        survey.setEarlyEndDate(LocalDateTime.now());
        survey.setStatus(SurveyStatus.DONE);
        removeSurveySchedule(survey);
        surveyRepository.save(survey);
    }

    /**
     * 설문 참여자가 제출한 설문 응답을 저장한다.
     */
    @Transactional
    public void submitSurvey(RequestSubmitSurveyDto requestSubmitSurveyDto, Long surveyId, User user) {
        // 1. 해당 설문지 찾기
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Attend attend;
        isValidSurvey(survey);

        if (survey.isPrivateSurvey()){
            // 1. 폐쇄형인 경우
            // 1-1. 해당 설문에 대한 참여 권한이 있는지
            attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, user.getEmail())
                    .orElseThrow(() -> new CustomException(ErrorCode.DOES_NOT_HAVE_PERMISSION));
            if (attend.getStatus() != AttendStatus.NONRESPONSE){
                throw new CustomException(ErrorCode.CANNOT_PARTICIPATE_SURVEY);
            }
            if (survey.isAskAge()){
                attend.setAge(requestSubmitSurveyDto.getAge());
            }
            if (survey.isAskGender()){
                attend.setGender(requestSubmitSurveyDto.getGender());
            }
            if (attend.getUser()==null){
                attend.setUser(user);
            }
            attend.setStatus(AttendStatus.RESPONSE);
            attend.setResponseDate(LocalDateTime.now());
            surveyAttendRepository.save(attend);

        }
        else{
            // 2. 오픈형인 경우
            // 2-1. 참여 제한에 걸려있는지?
            int participants = surveyAttendRepository.countBySurveyAndStatus(survey, AttendStatus.RESPONSE);
            if (survey.getLimitPerson() > 0 && survey.getLimitPerson() <= participants){
                throw new CustomException(ErrorCode.FIRST_COME_FIRST_SERVED_OVER);
            }
            Optional<Attend> optionalSurveyAttend = surveyAttendRepository.findBySurveyAndSendEmail(survey, user.getEmail());
            if(optionalSurveyAttend.isPresent()){
                attend = optionalSurveyAttend.get();
                if (attend.getStatus() != AttendStatus.NONRESPONSE){
                    throw new CustomException(ErrorCode.CANNOT_PARTICIPATE_SURVEY);
                }
                if (survey.isAskAge()){
                    attend.setAge(requestSubmitSurveyDto.getAge());
                }
                if (survey.isAskGender()){
                    attend.setGender(requestSubmitSurveyDto.getGender());
                }
                if (attend.getUser()==null){
                    attend.setUser(user);
                }
                attend.setStatus(AttendStatus.RESPONSE);
                attend.setResponseDate(LocalDateTime.now());
                surveyAttendRepository.save(attend);
            }else{
                attend = surveyAttendRepository.save(
                        Attend.builder()
                                .age(requestSubmitSurveyDto.getAge())
                                .gender(requestSubmitSurveyDto.getGender())
                                .sendEmail(user.getEmail())
                                .status(AttendStatus.RESPONSE)
                                .survey(survey)
                                .responseDate(LocalDateTime.now())
                                .user(user)
                                .build()
                );
            }

        }


        // 3. 각 질문별 저장하기
        // 3-1. 주관식 저장하기
        List<AnswerSub> answerSubList = requestSubmitSurveyDto.getSurveySubjective().stream()
                        .map(e -> {
                            SurveyQuestion surveyQuestion = surveyQuestionRepository.findByQuestionIdAndSurvey(e.getQuestionId(), survey).orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST_SUBMIT_QUESTION));
                            return new AnswerSub(surveyQuestion, attend, e.getValue());
                        })
                        .collect(Collectors.toList());
        answerSubRepository.saveAll(answerSubList);

        // 3-2. 객관식 저장하기
        List<AnswerMulti> answerMultiList = requestSubmitSurveyDto.getSurveyMultiple().stream()
                .map(e -> {
                    SurveyQuestion surveyQuestion = surveyQuestionRepository.findByQuestionIdAndSurvey(e.getQuestionId(), survey).orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST_SUBMIT_QUESTION));
                    QuestionOption questionOption = questionOptionRepository.findByOptionIdAndSurveyQuestion(e.getOptionId(), surveyQuestion).orElseThrow(() ->new CustomException(ErrorCode.BAD_REQUEST_SUBMIT_OPTION));
                    return new AnswerMulti(surveyQuestion, attend, questionOption);
                })
                .collect(Collectors.toList());
        answerMultiRepository.saveAll(answerMultiList);

    }

    void isValidSurvey(Survey survey) {
        // 1-1. 해당 설문에 응답 가능한 날짜인지
        LocalDateTime startDate = survey.getSurveyStartDate();
        LocalDateTime endDate = survey.getSurveyEndDate();
        LocalDateTime now = LocalDateTime.now();

        if(!isBetweenDay(now, startDate, endDate)){
            throw new CustomException(ErrorCode.DATE_NOT_AVAILABLE_RESPONSE);
        }

        // 1-2. 해당 설문이 응답 가능한 상태인지
        SurveyStatus status = survey.getStatus();
        if (status != SurveyStatus.PROGRESS){
            throw new CustomException(ErrorCode.SURVEY_STATUS_IS_NOT_PROGRESS);
        }

    }

    private Boolean isBetweenDay(LocalDateTime targetDate, LocalDateTime from, LocalDateTime to){
        return (targetDate.isAfter(from) || targetDate.isEqual(from)) && (targetDate.isBefore(to) || targetDate.isEqual(to));
    }
    public List<SurveyReceiverDto> getSurveyReceiver(Long surveyId) {
        List<Attend> attends = surveyAttendRepository.findBySurvey_SurveyId(surveyId);
        return attends.stream().map(SurveyReceiverDto::fromEntity).collect(Collectors.toList());
    }

    public void addSurveyReceiver(Long surveyId, RequestAddSurveyReceiverDto dto) {
        // 해당 설문이 있는지 확인
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));

        // 이미 추가된 이메일인지 확인
        Optional<Attend> attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, dto.getEmail());
        if (attend.isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_ALREADY_ADDED);
        }

        // 이메일 전송하는 부분 나중에 수정
        String[] emailList = {dto.getEmail()};
        HashMap<String, String> emailValues = getEmailInviteContext(survey);
        mailUtil.sendSurveyInviteMail(emailValues, emailList,surveyId);


        // 추가함
        surveyAttendRepository.save(Attend.builder()
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

    public void resendEmail(Long surveyId, RequestResendDto dto) {
        // 1. 설문지 검증
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        // 1-1. 상태값 확인
        if (survey.getStatus() != SurveyStatus.PROGRESS){
            throw new CustomException(ErrorCode.SURVEY_STATUS_IS_NOT_PROGRESS);
        }

        // 1-2. 끝난 설문은 이메일 재전송 불가능
        if (survey.getEarlyEndDate() != null){
            throw new CustomException(ErrorCode.EARLY_CLOSED_SURVEY);
        }

        // 1-3. 설문 응답 기간이 지난 설문은 이메일 재전송 불가능
        LocalDateTime endDate = survey.getSurveyEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)){
            throw new CustomException(ErrorCode.DATE_NOT_AVAILABLE_RESPONSE);
        }

        // 2. user 검증
        List<Attend> userList = surveyAttendRepository.findBySurveyAndStatusAndAttendIdIn(survey, AttendStatus.NONRESPONSE,dto.getAttendIdList());

        // 3. email 일괄 전송
        String[] emailList = userList.stream().map(Attend::getSendEmail).toArray(String[]::new);
        HashMap<String, String> emailValues = getEmailInviteContext(survey);

        mailUtil.sendSurveyInviteMail(emailValues, emailList,surveyId);



        // 4. send date 업데이트
        for(Attend attend: userList){
            attend.setSendDate(LocalDateTime.now());
        }
        surveyAttendRepository.saveAll(userList);

    }

    public void sendEmail(Long surveyId, RequestSendDto dto){
        // 1. 설문지 검증
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        // 1-1. 상태값 확인
        if (survey.getStatus() != SurveyStatus.PROGRESS){
            throw new CustomException(ErrorCode.SURVEY_STATUS_IS_NOT_PROGRESS);
        }

        // 1-2. 끝난 설문은 이메일 재전송 불가능
        if (survey.getEarlyEndDate() != null){
            throw new CustomException(ErrorCode.EARLY_CLOSED_SURVEY);
        }

        // 1-3. 설문 응답 기간이 지난 설문은 이메일 재전송 불가능
        LocalDateTime endDate = survey.getSurveyEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(endDate)){
            throw new CustomException(ErrorCode.DATE_NOT_AVAILABLE_RESPONSE);
        }


        // 2. user 만들기
        List<Attend> attendList = new ArrayList<>();
        List<String> sendEmails = new ArrayList<>();
        for(String email: dto.getSendEmailList()){
            Optional<Attend> attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, email);
            if (attend.isEmpty()){
                attendList.add(Attend.builder()
                        .sendDate(LocalDateTime.now())
                        .sendEmail(email)
                        .status(AttendStatus.NONRESPONSE)
                        .survey(survey)
                        .build());
                sendEmails.add(email);
            }
        }
        surveyAttendRepository.saveAll(attendList);


        // 3. email 일괄 전송
        String[] emailList = sendEmails.toArray(new String[0]);
        HashMap<String, String> emailValues = getEmailInviteContext(survey);
        mailUtil.sendSurveyInviteMail(emailValues, emailList, surveyId);
    }


    public HashMap<String, String> getEmailInviteContext(Survey survey){
        HashMap<String, String> emailValues = new HashMap<>();
        emailValues.put("senderName", survey.getUser().getNickname());
        emailValues.put("startDate", survey.getSurveyStartDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        emailValues.put("endDate", survey.getSurveyEndDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
        return emailValues;
    }

    public List<String> getAttendUserList(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new RuntimeException("해당 설문이 없습니다."));

        List<String> userList = new ArrayList<>();

        List<Attend> attendList = surveyAttendRepository.findBySurveyAndStatus(survey, AttendStatus.RESPONSE);

        for (Attend attend : attendList) {
            userList.add(attend.getSendEmail());
        }
        return userList;
    }

    public void rejectSurvey(Long surveyId, String email){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new CustomException(ErrorCode.SURVEY_NOT_FOUND));
        Attend attend = surveyAttendRepository.findBySurveyAndSendEmail(survey, email).orElseThrow(() -> new CustomException(ErrorCode.BAD_REQUEST));
        attend.setStatus(AttendStatus.REJECT);
        surveyAttendRepository.save(attend);
    }
}
