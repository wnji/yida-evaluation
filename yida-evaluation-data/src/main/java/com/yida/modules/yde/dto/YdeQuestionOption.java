package com.yida.modules.yde.dto;

import com.jeesite.common.collect.ListUtils;
import com.yida.modules.yde.entity.YdeOption;
import com.yida.modules.yde.entity.YdeQuestion;
import lombok.Data;

import java.util.*;

@Data
public class YdeQuestionOption {
    private String questionId;
    private Long no;
    private String question;
    private String optionName;
    private Integer optionType;
    private String optionContent;
    private Integer points;
    private Integer totalPoints;
    private String section;
    private String answer;

    public static List<YdeQuestion> toQuestions(List<YdeQuestionOption> questionOptions) {
        Map<String, YdeQuestion> questions = new LinkedHashMap<>();
        questionOptions.forEach(qo -> {
            YdeQuestion question;
            if(!questions.containsKey(qo.getQuestionId())){
                question = new YdeQuestion(qo.questionId);
                question.setQuestion(qo.getQuestion());
                question.setNo(qo.getNo());
                question.setSection(qo.getSection());
                question.setAnswer(qo.getAnswer());
                question.setOptionType(qo.getOptionType());
                question.setOptions(ListUtils.newArrayList());
                question.setPoints(qo.getTotalPoints());
                questions.put(qo.getQuestionId(), question);
            }else{
                question = questions.get(qo.getQuestionId());
            }
            YdeOption option = new YdeOption();
            option.setOptionName(qo.getOptionName());
            option.setContent(qo.getOptionContent());
            option.setPoints(qo.getPoints());
            question.getOptions().add(option);
        });

        for (YdeQuestion question : questions.values()) {
            if(YdeQuestion.OPTION_TYPE_SELECTION == question.getOptionType()) {
                question.setPoints(question.getOptions().stream().map(o ->
                        o.getPoints()).max(Comparator.comparing(Integer::valueOf)).get()
                );
            }
        }
        return new ArrayList<>(questions.values());
    }
}
