package scratch.BackEnd.components;


import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import scratch.BackEnd.domain.SurveySchedule;
import scratch.BackEnd.repository.SurveyScheduleRepository;
import scratch.BackEnd.service.SurveyService;

import java.time.LocalDateTime;


@Component
@RequiredArgsConstructor
public class SurveyScheduler {
	private final SurveyService surveyService;

	private final SurveyScheduleRepository surveyScheduleRepository;


	@Scheduled(cron = "0/10 * * * * *")
	public void checkSchedule(){
		System.out.println("scheduler active time : " + LocalDateTime.now() );
		SurveySchedule[] surveyScheduleList = surveyScheduleRepository.findAllByTimeBefore(LocalDateTime.now());

		System.out.print("스케줄 해야할거");
		for (SurveySchedule surveySchedule : surveyScheduleList) {
			System.out.print(surveySchedule.getScheduleId());
		}
		System.out.println();

		for (SurveySchedule surveySchedule : surveyScheduleList) {
			System.out.print("스케줄 처리중 : ");
			System.out.println(surveySchedule.getScheduleId());
			this.runSurveySchedule(surveySchedule);
		}
		finishSurveySchedule(surveyScheduleList);
	}

	public void runSurveySchedule(SurveySchedule surveySchedule){
		switch (surveySchedule.getAction()) {
			case PROGRESS:
				surveyService.startSurvey(surveySchedule.getSurvey().getSurveyId());
				break;
			case DONE:
				surveyService.closeSurvey(surveySchedule.getSurvey().getSurveyId());
				break;
			case MAKING:
				System.out.println("혹시 버그날까 둠");
				break;
		}
	}

	public void finishSurveySchedule(SurveySchedule[] surveyScheduleList){
		//surveyScheduleRepository.deleteAllInBatch(Arrays.asList(surveySchedule));     //soft delete 되게 쿼리 나중에 짬 ㄱㄷ
		for(SurveySchedule surveySchedule : surveyScheduleList){
			surveyScheduleRepository.delete(surveySchedule);
		}

	}

}
