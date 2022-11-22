package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;
import scratch.BackEnd.type.QuestionType;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestQuestionDto {

	private String title;
	private boolean required;
	private int ordering;
	private QuestionType type;
	private int optionNumber;
	private QuestionOptionDto[] optionList;

	public SurveyQuestion toEntity(Survey survey) {
		return SurveyQuestion.builder()
				.survey(survey)
				.title(title)
				.isRequired(required)
				.ordering(ordering)
				.questionType(type)
				.optionNumber(optionNumber)
				.build();
	}

}
