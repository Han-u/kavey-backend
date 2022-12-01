package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveySchedule;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Repository
public interface SurveyScheduleRepository extends JpaRepository<SurveySchedule, Long> {
	SurveySchedule[] findAllByTime(LocalDateTime now);

	@Transactional
	void deleteAllBySurvey(Survey survey);

	SurveySchedule[] findAllByTimeBefore(LocalDateTime now);
}