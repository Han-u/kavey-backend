package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import scratch.BackEnd.domain.QuestionOption;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.List;

public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
	List <QuestionOption> findBySurveyQuestion(SurveyQuestion surveyQuestion);
}