package scratch.BackEnd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestSubmitSurveyDto {
    private List<SurveySubjectiveTemplate> surveySubjective;
    private List<SurveyTemplate> surveyMultiple;
    private Long userId;
    private String gender;
    private String age;
}
