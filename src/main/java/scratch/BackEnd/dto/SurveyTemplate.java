package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.type.QuestionType;

@Data
public class SurveyTemplate {
    private Long questionId;
    private Long optionId;
    private QuestionType questionType;
}
