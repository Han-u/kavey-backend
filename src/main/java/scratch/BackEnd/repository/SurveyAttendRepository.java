package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyAttend;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyAttendRepository extends JpaRepository<SurveyAttend, Long> {
    List<SurveyAttend> findBySurvey_SurveyId(Long surveyId);
    Optional<SurveyAttend> findBySurveyAndSendEmail(Survey survey, String sendEmail);
    Integer countBySurveyAndStatus(Survey survey, AttendStatus status);
}
