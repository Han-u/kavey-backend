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
    private SurveyAttend surveyAttend;

//    private Long surveyId;
    private String value; //주관식 답변

    public AnswerSub(SurveyQuestion surveyQuestion, SurveyAttend surveyAttend, String value){
        this.surveyQuestion = surveyQuestion;
        this.surveyAttend = surveyAttend;
        this.value = value;
    }
}
