package scratch.BackEnd.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SurveyTemplateTypes {
    TEXT("주관식"),
    RADIO("객관식"),
    CHECKBOX("다중선택"),
    RATING("평점별점"),
    NUMBER("평점숫자");

    private String label;

}
