package scratch.BackEnd.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import scratch.BackEnd.domain.Survey;
import scratch.BackEnd.domain.User;
import scratch.BackEnd.dto.SurveyListDto;

import java.util.List;


@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    List<Survey> findAll();
    @Query("SELECT s FROM Survey s WHERE s.user.kakaoid=:kakaoid")
    List<Survey> findByUserid(@Param("kakaoid") Long kakaoid);


}
