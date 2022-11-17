package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;

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
    private String status; //응답,미응답,거절
    private String responseDate; //설문 응답 시간
    private String sendDate; //설문 보낸 시간
    private String gender; //성별
    private String age; //나이
}
