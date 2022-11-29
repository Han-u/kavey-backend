package scratch.BackEnd.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import scratch.BackEnd.type.SurveyStatus;
import scratch.BackEnd.type.SurveyTheme;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE survey SET is_deleted=true WHERE survey_id = ?")
public class Survey extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy= "survey", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval=true)
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>(); // 읽기만 가능

    private String title;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private LocalDateTime earlyEndDate;
    private String description;
    private int questionNumber;
    private int limitPerson; //참여인원
    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    @Enumerated(EnumType.STRING)
    private SurveyTheme theme;
    private boolean isPrivate; //폐쇠,개방
    private boolean askGender; //성별포함
    private boolean askAge; //나이포함
    private boolean isDeleted = Boolean.FALSE;

    @Builder
    public Survey(User user, String title, String description, boolean askAge,
                  boolean askGender, boolean isPrivate, int limitPerson, LocalDateTime startDate, LocalDateTime endDate, int questionNumber, SurveyStatus status, SurveyTheme theme) {
        this.user = user;
        this.title = title;
        this.status = status;
        this.description = description;
        this.askAge = askAge;
        this.askGender = askGender;
        this.isPrivate = isPrivate;
        this.limitPerson = limitPerson;
        this.surveyStartDate = startDate;
        this.surveyEndDate = endDate;
        this.questionNumber = questionNumber;
        this.theme = theme;
    }

    public void update(Survey survey)
    {
        this.user = survey.getUser();
        this.title = survey.getTitle();
        this.status = survey.getStatus();
        this.description = survey.getDescription();
        this.askAge = survey.isAskAge();
        this.askGender = survey.isAskGender();
        this.isPrivate = survey.isPrivate();
        this.limitPerson = survey.getLimitPerson();
        this.surveyStartDate = survey.getSurveyStartDate();
        this.surveyEndDate = survey.getSurveyEndDate();
        this.questionNumber = survey.getQuestionNumber();
        this.theme = survey.getTheme();
    }
}
