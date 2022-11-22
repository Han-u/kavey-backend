package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyStatus;
import scratch.BackEnd.domain.User;

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
	private boolean isPrivate;
	private int limitPerson;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private int theme;
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
				.isPrivate(isPrivate)
				.limitPerson(limitPerson)
				.surveyStartDate(startDate)
				.surveyEndDate(endDate)
				.questionNumber(questionNumber)
				.themeType(theme)
				.build();
	}

}
