package scratch.BackEnd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    SURVEY_NOT_FOUND(404, "S001", "설문을 찾을 수 없습니다.");

    private int status;
    private String code;
    private String message;

}
