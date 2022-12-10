package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.AnswerMulti;
import scratch.BackEnd.domain.Attend;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.List;

public interface AnswerMultiRepository extends JpaRepository<AnswerMulti, Long> {
	List<AnswerMulti> findAllByAttend(Attend attend);
	List<AnswerMulti> findBySurveyQuestion(SurveyQuestion surveyQuestion);
}
