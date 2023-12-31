package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.SurveyQuestion;

import javax.transaction.Transactional;
import java.util.Optional;

import java.util.List;


public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    Optional<SurveyQuestion> findByQuestionIdAndSurvey(Long questionId, Survey survey);

	List<SurveyQuestion> findBySurvey(Survey survey);

	long deleteBySurvey(Survey survey);


	List<SurveyQuestion> findAllBySurvey(Survey survey);
}
