package scratch.BackEnd.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE survey SET is_deleted=true WHERE survey_id = ?")
public class QuestionOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name="question_id")
    private SurveyQuestion surveyQuestion;
    private String value; //보기
    private int ordering;
    private String data;
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public QuestionOption(SurveyQuestion question, String value, int ordering, String data) {
        this.surveyQuestion = question;
        this.value = value;
        this.ordering = ordering;
        this.data = data;
    }
}
