package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestQuestionDto {

	private String title;
	private boolean is_required;
	private int ordering;
	private int type;
	private int option_number;
	private String option_list[];

	public SurveyQuestion toEntity(Survey survey) {
		return SurveyQuestion.builder()
				.survey(survey)
				.title(title)
				.isRequried(is_required)
				.ordering(ordering)
//				.questionTypes(type)
				.optionNumber(option_number)
				.build();
	}

}
