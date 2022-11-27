package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.type.AttendStatus;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.Attend;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyAttendRepository extends JpaRepository<Attend, Long> {
    List<Attend> findBySurvey_SurveyId(Long surveyId);
    Optional<Attend> findBySurveyAndSendEmail(Survey survey, String sendEmail);
    Integer countBySurveyAndStatus(Survey survey, AttendStatus status);

    List<Attend> findBySurveyAndStatusAndUser_IdIn(Survey survey, AttendStatus status,List<Long> userIdList);
}
