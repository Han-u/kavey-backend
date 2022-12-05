package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.AnswerMulti;
import scratch.BackEnd.domain.AnswerSub;
import scratch.BackEnd.type.QuestionType;

/**
 * A DTO for the {@link org.springframework.data.jpa.domain.AbstractPersistable} entity
 */
@Data
public class ResponseUserAnswerDto {
	private final Long userId;
	private final Long questionId;
	private final String answer;
	private final QuestionType questionType;
	private final String questionType_string;

	public ResponseUserAnswerDto(long userId, AnswerSub answerSub, QuestionType questionType){
		this.userId = userId;
		this.questionId = answerSub.getSurveyQuestion().getQuestionId();
		this.answer = answerSub.getValue();
		this.questionType = questionType;
		this.questionType_string = "Sub";
	}
	public ResponseUserAnswerDto(long userId, AnswerMulti answerMulti, QuestionType questionType){
		this.userId = userId;
		this.questionId = answerMulti.getSurveyQuestion().getQuestionId();
		this.answer = answerMulti.getQuestionOption().getOptionId().toString();
		this.questionType = questionType;
		this.questionType_string = "Multi";
	}

}