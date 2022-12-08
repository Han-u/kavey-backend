package scratch.BackEnd.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link org.springframework.data.jpa.domain.AbstractPersistable} entity
 */
@Data
public class ResponseUserAnswerTotalDto implements Serializable {
	private final Long userId;
	private final Long surveyId;
	private final Long attendId;

	private final List<ResponseUserAnswerDto> responseUserAnswerDtos;



}