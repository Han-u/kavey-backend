package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.AttendStatus;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyAttend;

import java.util.Optional;

@Repository
public interface SurveyAttendRepository extends JpaRepository<SurveyAttend, Long> {
    Optional<SurveyAttend> findBySurveyAndSendEmail(Survey survey, String sendEmail);
    Integer countBySurveyAndStatus(Survey survey, AttendStatus status);
}
