package scratch.BackEnd.dto;

import lombok.Data;

@Data
public class SurveyTemplate {
    private Long questionId;
    private Long optionId;
    private Long questionTypeId;
}
