package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.User;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestSurveyDto {

	private Long user_id;
	private String title;
	private String description;
	private boolean ask_age;
	private boolean ask_gender;
	private boolean is_private;
	private int limit_person;
	private LocalDateTime start_date;
	private LocalDateTime end_date;
	private int question_number;
	private RequestQuestionDto question_list[];

	public Survey toEntity(User user){
		return Survey.builder()
				.user(user)
				.title(title)
				.description(description)
				.askAge(ask_age)
				.askGender(ask_gender)
				.isPrivate(is_private)
				.limitPerson(limit_person)
				.surveyStartDate(start_date)
				.surveyEndDate(end_date)
				.questionNumber(question_number)
				.build();
	}

}
