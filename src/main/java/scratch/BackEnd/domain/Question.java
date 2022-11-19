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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy="question")
    private List<QuestionType> questionTypes = new ArrayList<>();

    private String title;
    private String ordering;
    private boolean isDeleted = Boolean.FALSE;
    private String isRequried;

    @Builder
    public Question(Survey survey, QuestionType questionType, String title, String ordering, String isRequried) {
        this.survey = survey;
        this.questionTypes = questionTypes;
        this.title = title;
        this.ordering = ordering;
        this.isRequried = isRequried;
    }
}
