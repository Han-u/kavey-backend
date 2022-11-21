package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.QuestionType;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestQuestionDto {

	private String title;
	private boolean required;
	private boolean foo;
	private int ordering;
	private long type;
	private int option_number;
	private QuestionOptionDto[] option_list;

	public SurveyQuestion toEntity(Survey survey, QuestionType type ) {
		return SurveyQuestion.builder()
				.survey(survey)
				.title(title)
				.isRequired(required)
				.ordering(ordering)
				.questionType(type)
				.optionNumber(option_number)
				.build();
	}

}
