package scratch.BackEnd.dto;

import lombok.*;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.domain.SurveyAttend;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SurveyReceiverDto {
    private Long attendID;
    private String sendEmail;
    private AttendStatus status;
    private LocalDateTime responseDate;
    private LocalDateTime sendDate;

    public static SurveyReceiverDto fromEntity(SurveyAttend attend){
        return SurveyReceiverDto.builder()
                .attendID(attend.getAttendId())
                .sendEmail(attend.getSendEmail())
                .status(attend.getStatus())
                .responseDate(attend.getResponseDate())
                .sendDate(attend.getSendDate())
                .build();
    }
}
