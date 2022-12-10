package scratch.BackEnd.dto;

import lombok.Data;

@Data
public class AnswerCountDto {
	private final String Value;
	private int count;

	public AnswerCountDto(String Value, int count){
		this.Value =Value;
		this.count = count;
	}
}
