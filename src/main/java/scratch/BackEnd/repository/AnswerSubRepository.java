package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.AnswerSub;
import scratch.BackEnd.domain.Attend;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.List;


public interface AnswerSubRepository extends JpaRepository<AnswerSub, Long> {

	List<AnswerSub> findAllByAttend(Attend attend);

	List<AnswerSub> findBySurveyQuestion(SurveyQuestion question);






}