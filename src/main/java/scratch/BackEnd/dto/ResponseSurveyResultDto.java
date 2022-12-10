package scratch.BackEnd.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseSurveyResultDto {
		private final Long surveyId;
		private final int attendCount;
		private final List<ResponseQuestionResultDto> responseQuestionResultList;
		public ResponseSurveyResultDto(Long surveyId, int attendCount, List<ResponseQuestionResultDto> responseQuestionResultList) {
		    this.surveyId = surveyId;
			this.attendCount = attendCount;
			this.responseQuestionResultList = responseQuestionResultList;
		}

}
