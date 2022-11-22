package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.SurveyQuestion;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link scratch.BackEnd.domain.SurveyQuestion} entity
 */
@Data
public class ResponseQuestionDto implements Serializable {
	private final Long questionId;
	private final String title;
	private final int ordering;
	private final boolean isRequired;
	private final int optionNumber;
	private final List<ResponseQuestionOptionDto> responseQuestionOptionDtos;

	public ResponseQuestionDto(SurveyQuestion surveyQuestion, List<ResponseQuestionOptionDto> responseQuestionOptionDtos){
		this.questionId = surveyQuestion.getQuestionId();
		this.title = surveyQuestion.getTitle();
		this.ordering = surveyQuestion.getOrdering();
		this.isRequired = surveyQuestion.isRequired();
		this.optionNumber = surveyQuestion.getOptionNumber();
		this.responseQuestionOptionDtos = responseQuestionOptionDtos;
	}

}