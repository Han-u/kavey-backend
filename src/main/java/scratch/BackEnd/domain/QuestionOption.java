package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name="qeustion_id")
    private SurveyQuestion surveyQuestion;
    private String value; //보기
    private int ordering;
    private String data;

    @Builder
    public QuestionOption(SurveyQuestion question, String value, int ordering, String data) {
        this.surveyQuestion = question;
        this.value = value;
        this.ordering = ordering;
        this.data = data;
    }
}
