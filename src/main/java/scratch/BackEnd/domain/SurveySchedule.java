package scratch.BackEnd.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import scratch.BackEnd.type.SurveyStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "survey_schedule", indexes = {@Index(name = "i_date", columnList = "time")})
@Where(clause = "is_deleted = false")
@SQLDelete(sql= "UPDATE survey_schedule SET is_deleted=true WHERE schedule_id = ?")
public class SurveySchedule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_Id", nullable = false)
	private Long scheduleId;
	private LocalDateTime time;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "survey_id")
	private Survey survey;

	@Enumerated(EnumType.STRING)
	private SurveyStatus action;
	private boolean isDeleted = Boolean.FALSE;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	@Builder
	public SurveySchedule(Survey survey, LocalDateTime time, SurveyStatus action){
		this.survey = survey;
		this.time = time;
		this.action = action;
	}

}