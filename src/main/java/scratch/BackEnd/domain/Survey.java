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
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE survey SET is_deleted=true WHERE survey_id = ?")
@ToString
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy= "survey")
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>(); // 읽기만 가능

    private String title;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private LocalDateTime earlyEndDate;
    private String description;
    private int questionNumber;
    private int themeType;
    private int limitPerson; //참여인원
    @Enumerated(EnumType.STRING)
    private SurveyStatus status;
    private boolean isPrivate; //폐쇠,개방
    private boolean askGender; //성별포함
    private boolean askAge; //나이포함
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Survey(User user, String title, String description, boolean ask_age,
                  boolean ask_gender, boolean is_private, int limit_person, LocalDateTime start_date, LocalDateTime end_date, int question_number, int themeType, SurveyStatus status) {
        this.user = user;
        this.title = title;
        this.status = status;
        this.description = description;
        this.askAge = ask_age;
        this.askGender = ask_gender;
        this.isPrivate = is_private;
        this.limitPerson = limit_person;
        this.surveyStartDate = start_date;
        this.surveyEndDate = end_date;
        this.questionNumber = question_number;
        this.themeType = themeType;
    }
}
