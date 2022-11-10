package scratch.BackEnd.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long surveyId;

    private Long userId;
    private String title;
    private LocalDateTime surveyStartDate;
    private LocalDateTime surveyEndDate;
    private String description;
    private boolean isPrivate;
    private int themeType;
    private int limitPerson;
    private boolean askGender;
    private boolean askAge;
    private boolean isDeleted;
    private String status;
    private LocalDateTime earlyEndDate;




}
