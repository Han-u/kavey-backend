package scratch.BackEnd.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = true")
@SQLDelete(sql= "UPDATE question SET is_deleted=true WHERE questionId = ?")
@ToString
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy= "surveyQuestion")
    private List<QuestionType> questionTypes = new ArrayList<>();

    private String title;
    private int ordering;
    private boolean isDeleted = Boolean.FALSE;
    private boolean isRequried;
    private int optionNumber;

    @Builder
    public SurveyQuestion(Survey survey, QuestionType questionType, String title, int ordering, boolean is_Requried, int optionNumber) {
        this.survey = survey;
//        this.questionTypes = questionType;
        this.title = title;
        this.ordering = ordering;
        this.isRequried = is_Requried;
        this.optionNumber = optionNumber;

    }
}
