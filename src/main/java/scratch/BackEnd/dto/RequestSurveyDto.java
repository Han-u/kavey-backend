package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.type.SurveyStatus;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.type.SurveyTheme;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestSurveyDto {

	private Long userId;
	private String title;
	private String description;
	private boolean askAge;
	private boolean askGender;
	private boolean privateSurvey;
	private int limitPerson;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private SurveyTheme theme;
	private int questionNumber;

	private RequestQuestionDto[] questionList;

	public Survey toEntity(User user, SurveyStatus status){
		return Survey.builder()
				.user(user)
				.title(title)
				.description(description)
				.status(status)
				.askAge(askAge)
				.askGender(askGender)
				.privateSurvey(privateSurvey)
				.limitPerson(limitPerson)
				.surveyStartDate(startDate)
				.surveyEndDate(endDate)
				.questionNumber(questionNumber)
				.theme(theme)
				.build();
	}

}
