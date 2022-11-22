package scratch.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyListDto {
    private String title;
    private boolean isPrivate;
    private int limitPerson;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private LocalDateTime earlyEndDate;
    private SurveyStatus status;


    public static SurveyListDto fromEntity(Survey survey){
        return SurveyListDto.builder()
                .title(survey.getTitle())
                .isPrivate(survey.isPrivate())
                .limitPerson(survey.getLimitPerson())
                .surveyStartDate(survey.getSurveyStartDate())
                .surveyEndDate(survey.getSurveyEndDate())
                .earlyEndDate(survey.getEarlyEndDate())
                .status(survey.getStatus())
                .build();
    }
}
