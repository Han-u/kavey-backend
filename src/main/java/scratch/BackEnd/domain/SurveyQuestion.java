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
@SQLDelete(sql= "UPDATE question SET is_deleted=true WHERE questionId = ?")
@ToString
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

//    @OneToMany(mappedBy= "surveyQuestion")
//    private List<QuestionType> questionTypes = new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "questionType_id")
    private QuestionType questionType;


    private String title;
    private int ordering;
    private boolean isDeleted = Boolean.FALSE;
    private boolean isRequired;
    private int optionNumber;

    @Builder
    public SurveyQuestion(Survey survey, QuestionType questionType, String title, int ordering, boolean is_required, int optionNumber) {
        this.survey = survey;
        this.questionType = questionType;
        this.title = title;
        this.ordering = ordering;
        this.isRequired = is_required;
        this.optionNumber = optionNumber;

    }
}
