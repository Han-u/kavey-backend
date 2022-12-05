package scratch.BackEnd.dto;

import lombok.Data;
import scratch.BackEnd.domain.AnswerMulti;
import scratch.BackEnd.domain.AnswerSub;
import scratch.BackEnd.domain.SurveyQuestion;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ResponseQuestionAnswerDto {
    private Long questionId;
    private List<AnswerMultiDto> answerMultis;
    private List<AnswerSubDto> answerSubs;

    public ResponseQuestionAnswerDto(SurveyQuestion surveyQuestion, List<AnswerMulti> answerMultis, List<AnswerSub> answerSubs){
        this.questionId = surveyQuestion.getQuestionId();
        this.answerMultis = answerMultis.stream().map(AnswerMultiDto::new).collect(Collectors.toList());
        this.answerSubs = answerSubs.stream().map(AnswerSubDto::new).collect(Collectors.toList());

    }

    @Data
    public class AnswerMultiDto{
        private Long multiId;
        private Long attendId;
        private Long optionId;
        private Long questionId;

        public AnswerMultiDto(AnswerMulti answerMulti){
            this.multiId = answerMulti.getMultiId();
            this.optionId = answerMulti.getQuestionOption().getOptionId();
            this.attendId = answerMulti.getAttend().getAttendId();
            this.questionId = answerMulti.getSurveyQuestion().getQuestionId();
        }
    }

    @Data
    public class AnswerSubDto{
        private Long subId;
        private String value;
        private Long attendId;
        private Long questionId;

        public AnswerSubDto(AnswerSub answerSub){
            this.subId = answerSub.getSubId();
            this.value = answerSub.getValue();
            this.attendId = answerSub.getAttend().getAttendId();
            this.questionId = answerSub.getSurveyQuestion().getQuestionId();
        }
    }
}
