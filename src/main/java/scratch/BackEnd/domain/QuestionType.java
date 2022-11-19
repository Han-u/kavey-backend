package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionType {

    @Id
    private Long questionTypeId;

    @ManyToOne
    @JoinColumn(name="question_id")
    private SurveyQuestion surveyQuestion;

    @Column(nullable = false)
    private String typeName;

}
