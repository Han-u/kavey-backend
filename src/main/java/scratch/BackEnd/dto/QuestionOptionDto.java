package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.QuestionOption;
import scratch.BackEnd.domain.SurveyQuestion;

import java.io.Serializable;

/**
 * A DTO for the {@link scratch.BackEnd.domain.QuestionOption} entity
 */
@Data
public class QuestionOptionDto implements Serializable {
	private final Long optionId;
	private final String value;
	private final int ordering;

	public QuestionOption toEntity(SurveyQuestion question){
		return QuestionOption.builder()
				.surveyQuestion(question)
				.value(value)
				.ordering(ordering)
				.build();
	}
}