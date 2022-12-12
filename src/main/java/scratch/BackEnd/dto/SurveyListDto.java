package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.type.SurveyStatus;

import java.time.LocalDateTime;

@Getter
public class SurveyListDto {
    private Long surveyId;
    private Long userid;
    private String title;
    private boolean isPrivate;
    private int limitPerson;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private LocalDateTime earlyEndDate;
    private SurveyStatus status;


    public SurveyListDto (Survey survey){
        this.surveyId = survey.getSurveyId();
        this.userid = survey.getUser().getId();
        this.title = survey.getTitle();
        this.isPrivate = survey.isPrivateSurvey();
        this.limitPerson = survey.getLimitPerson();
        this.surveyStartDate = survey.getSurveyStartDate();
        this.surveyEndDate = survey.getSurveyEndDate();
        this.earlyEndDate = survey.getEarlyEndDate();
        this.status = survey.getStatus();

    }
}
