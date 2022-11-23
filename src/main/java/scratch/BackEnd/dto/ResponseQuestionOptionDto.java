package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.QuestionOption;

import java.io.Serializable;

/**
 * A DTO for the {@link scratch.BackEnd.domain.QuestionOption} entity
 */
@Data
public class ResponseQuestionOptionDto implements Serializable {
	private final Long optionId;
	private final String value;
	private final int ordering;
	private final String data;

	public ResponseQuestionOptionDto(QuestionOption questionOption){
		this.optionId = questionOption.getOptionId();
		this.value = questionOption.getValue();
		this.ordering = questionOption.getOrdering();
		this.data = questionOption.getData();
	}
}