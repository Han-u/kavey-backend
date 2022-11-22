package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.List;


public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {

	List<SurveyQuestion> findBySurvey(Survey survey);

}
