package scratch.BackEnd.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.SurveyQuestion;


public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {


}
