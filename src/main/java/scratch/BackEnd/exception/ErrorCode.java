package scratch.BackEnd.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    BAD_REQUEST(400, "S000", "잘못된 요청입니다."),
    SURVEY_NOT_FOUND(404, "S001", "설문을 찾을 수 없습니다."),
    PROGRESS_CANNOT_BE_MODIFIED(400, "S002", "진행 중인 설문은 수정이 불가능합니다."),
    PROGRESS_CANNOT_BE_DELETE(400, "S003", "진행 중인 설문은 삭제가 불가능합니다."),
    ONLY_PROGRESS_CAN_EARLY_CLOSE(400, "S004", "진행 중인 설문만 조기 종료가 가능합니다."),
    SURVEY_ALREADY_BEEN_COMPLETED(400, "S005", "이미 종료된 설문입니다."),
    FIRST_COME_FIRST_SERVED_OVER(400, "S006", "선착순 마감된 설문입니다."),
    DATE_NOT_AVAILABLE_RESPONSE(400, "S007", "설문 응답 가능한 날짜가 아닙니다."),
    SURVEY_STATUS_IS_NOT_PROGRESS(400, "S008", "진행 중인 설문이 아닙니다."),
    EMAIL_ALREADY_ADDED(400, "S009", "이미 추가된 이메일 입니다."),
    EARLY_CLOSED_SURVEY(400, "S010", "조기 마감된 설문입니다."),
    BAD_REQUEST_SUBMIT_QUESTION(400, "S011", "잘못된 요청입니다 - 설문지에 없는 질문번호 요청"),
    BAD_REQUEST_SUBMIT_OPTION(400, "S012", "잘못된 요청입니다 - 질문에 없는 응답 옵션 선택"),
    CANNOT_PARTICIPATE_SURVEY(400, "S013", "이미 참여했거나, 거절한 설문입니다."),


    UNAUTHORIZED(401, "U000", "인증 토큰이 올바르지 않습니다."),
    USER_NOT_FOUND(404, "U001", "유저를 찾을 수 없습니다"),
    DOES_NOT_HAVE_PERMISSION(400, "U002", "권한이 없습니다."),
    UNABLE_TO_ANSWER(400, "U003", "응답할 수 없는 설문입니다.");


    private int status;
    private String code;
    private String message;

}
