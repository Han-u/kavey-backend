package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionType {

    @Id
    private Long questionTypeId;

    @Column(nullable = false)
    private String typeName;

}
