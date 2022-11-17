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
@Where(clause = "is_deleted = true")
@SQLDelete(sql= "UPDATE question SET is_deleted=true WHERE questionId = ?")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name="survey_id")
    private Survey survey;
    private String title;
    private String ordering;
    private boolean isDeleted = Boolean.FALSE;
    private String isRequried;

}
