package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.Survey;

import java.util.List;

@Data
public class ResponseAllAnswerDto {
    private final Long surveyId;
    private final List<ResponseQuestionAnswerDto> questionResults;

    public ResponseAllAnswerDto(Survey survey, List<ResponseQuestionAnswerDto> responseQuestionAnswerDtos){
        this.surveyId = survey.getSurveyId();
        this.questionResults = responseQuestionAnswerDtos;
    }
}
