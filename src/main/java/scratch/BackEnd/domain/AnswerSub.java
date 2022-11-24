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
    @JoinColumn(name="question_id")
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    @JoinColumn(name="attend_id")
    private Attend attend;

//    private Long surveyId;
    private String value; //주관식 답변

    public AnswerSub(SurveyQuestion surveyQuestion, Attend attend, String value){
        this.surveyQuestion = surveyQuestion;
        this.attend = attend;
        this.value = value;
    }
}
