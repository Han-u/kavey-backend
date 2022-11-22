package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerMulti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long multiId;

    @OneToOne
    @JoinColumn(name="question_id")
    private SurveyQuestion surveyQuestion;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

//    private Long surveyId;

    @ManyToOne
    @JoinColumn(name="option_id")
    private QuestionOption questionOption; //객관식 답변

    public AnswerMulti(SurveyQuestion surveyQuestion, User user, QuestionOption questionOption){
        this.surveyQuestion = surveyQuestion;
        this.user = user;
        this.questionOption = questionOption;
    }
}
