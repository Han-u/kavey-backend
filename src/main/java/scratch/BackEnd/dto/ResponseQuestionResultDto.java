package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.type.QuestionType;

import java.util.ArrayList;
import java.util.List;

@Data
public class ResponseQuestionResultDto {

	private final Long questionId;
	private final QuestionType type;
	private final int attendCount;
	private List<AnswerCountDto> answerCountList;

	public ResponseQuestionResultDto(Long questionId,  QuestionType type, int attendCount, List<AnswerCountDto> answerCountList){
		this.questionId = questionId;
		this.type = type;
		this.attendCount = attendCount;
		this.answerCountList = answerCountList;
	}
}
