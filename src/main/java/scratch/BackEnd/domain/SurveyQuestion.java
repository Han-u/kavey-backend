package scratch.BackEnd.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import scratch.BackEnd.type.QuestionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE survey_question SET is_deleted=true WHERE question_id = ?")
@ToString
public class SurveyQuestion extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy= "surveyQuestion", cascade = CascadeType.ALL, orphanRemoval=true)
    private List<QuestionOption> questionOptions = new ArrayList<>(); // 읽기만 가능
    @Enumerated(EnumType.STRING)
    private QuestionType questionType;


    private String title;
    private int ordering;
    private boolean isDeleted = Boolean.FALSE;
    private boolean isRequired;
    private int optionNumber;

    @Builder
    public SurveyQuestion(Survey survey, QuestionType questionType, String title, int ordering, boolean isRequired, int optionNumber) {
        this.survey = survey;
        this.questionType = questionType;
        this.title = title;
        this.ordering = ordering;
        this.isRequired = isRequired;
        this.optionNumber = optionNumber;
    }


}
