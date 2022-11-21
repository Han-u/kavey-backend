package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.domain.AttendStatus;
import scratch.BackEnd.domain.SurveyAttend;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyReceiverDto {
    private Long attendID;
    private String sendEmail;
    private AttendStatus status;
    private String responseDate;
    private String sendDate;

    public static SurveyReceiverDto fromEntity(SurveyAttend attend){
        return SurveyReceiverDto.builder()
                .attendID(attend.getAttendId())
                .sendEmail(attend.getSendEmail())
                .status(attend.getStatus())
                .responseDate(attend.getResponseDate().toString())
                .sendDate(attend.getSendDate().toString())
                .build();
    }
}
