package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.Question;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class RequestQuestionDto {

	private String title;
	private boolean is_required;
	private int ordering;
	private int type;
	private int option_number;
	private String option_list[];

	public Question toEntity(){
		return Question.builder().title(title).build();

	}

}
