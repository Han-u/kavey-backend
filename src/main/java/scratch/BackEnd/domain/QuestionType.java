package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionType {

    @Id
    private Long questionTypeId;

//    @ManyToOne                            //머임?
//    @JoinColumn(name="question_id")
//    private SurveyQuestion surveyQuestion;

    @OneToMany(mappedBy = "questionType")
    private List<SurveyQuestion> surverQuestions = new ArrayList<>();


    @Column(nullable = false)
    private String typeName;

}
