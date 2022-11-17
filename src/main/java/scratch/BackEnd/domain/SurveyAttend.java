package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyAttend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long AttendId;

    private Long surveyId;
    private Long userId;
    private String status; 
    private String responseDate; //설문 응답 시간
    private String sendDate; //설문 보낸 시간
    private String gender; //성별
    private String age; //나이
}
