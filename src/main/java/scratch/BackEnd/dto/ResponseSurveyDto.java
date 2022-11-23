package scratch.BackEnd.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Getter
@Setter

public class ResponseSurveyDto {
	private final Long surveyId;
	private final List<ResponseQuestionDto> ResponseSurveyQuestions;
	private final String title;
	private final LocalDateTime surveyStartDate;
	private final LocalDateTime surveyEndDate;
	private final String description;
	private final int questionNumber;
	private final int themeType;
	private final int limitPerson;
	private final SurveyStatus status;
	private final boolean isPrivate;
	private final boolean askGender;
	private final boolean askAge;

	public ResponseSurveyDto(Survey survey, List<ResponseQuestionDto> responseQuestionDto){
		this.surveyId = survey.getSurveyId();
		this.title = survey.getTitle();
		this.description = survey.getDescription();
		this.surveyStartDate = survey.getSurveyStartDate();
		this.surveyEndDate = survey.getSurveyEndDate();
		this.isPrivate = survey.isPrivate();
		this.askGender = survey.isAskGender();
		this.askAge = survey.isAskAge();
		this.questionNumber = survey.getQuestionNumber();
		this.ResponseSurveyQuestions = responseQuestionDto;
		this.themeType = survey.getThemeType();
		this.limitPerson = survey.getLimitPerson();
		this.status = survey.getStatus();
	}

}