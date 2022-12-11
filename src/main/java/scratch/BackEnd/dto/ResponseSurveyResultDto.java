package scratch.BackEnd.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseSurveyResultDto {
		private final Long surveyId;
		private final int attendCount;

		private final ResponseQuestionResultDto genderResult;
		private final ResponseQuestionResultDto ageResult;

		private final List<ResponseQuestionResultDto> responseQuestionResultList;
		public ResponseSurveyResultDto(Long surveyId, int attendCount, ResponseQuestionResultDto genderResult,  ResponseQuestionResultDto ageResult, List<ResponseQuestionResultDto> responseQuestionResultList) {
		    this.surveyId = surveyId;
			this.attendCount = attendCount;
			this.genderResult = genderResult;
			this.ageResult = ageResult;
			this.responseQuestionResultList = responseQuestionResultList;
		}

}
