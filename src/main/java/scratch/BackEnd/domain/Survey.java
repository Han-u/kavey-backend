package scratch.BackEnd.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "is_deleted = true")
@SQLDelete(sql= "UPDATE survey SET is_deleted=true WHERE surveyId = ?")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy= "survey")
    private List<Question> questions = new ArrayList<>(); // 읽기만 가능

    private String title;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private LocalDateTime earlyEndDate;
    private String description;
    private int themeType;
    private int limitPerson; //참여인원
    private String status;
    private boolean isPrivate; //폐쇠,개방
    private boolean askGender; //성별포함
    private boolean askAge; //나이포함
    private boolean isDeleted = Boolean.FALSE;

}
