package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyAttend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    private String sendEmail;
    @Enumerated(EnumType.STRING)
    private AttendStatus status;
    private LocalDateTime responseDate; //설문 응답 시간
    private LocalDateTime sendDate; //설문 보낸 시간
    private String gender; //성별
    private String age; //나이
}
