package scratch.BackEnd.domain;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerSub {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subId;

    @OneToOne
    @JoinColumn(name="qestion_id")
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    private Long surveyId;
    private String value; //주관식 답변

}
