package com.yida.modules.yde.report;

import com.yida.modules.yde.api.v1.model.YdeAnswer;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.service.YdeRuleService;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class AbstractReportBuilder implements ReportBuilder{
    protected String errorCode = null;
    protected String errorMessage = null;
    protected List<YdeAnswer> answers;
    protected List<YdeQuestion> questions;
    protected Map<String, YdeQuestion> questionIdMap;
    protected Integer totalScore;

    @Setter
    protected YdeRule rule;

    @Setter
    @Getter
    protected YdeEvaluation evaluation;

    @Setter
    @Getter
    protected YdeModule module;

    @Setter
    protected YdeRuleService ydeRuleService;

    @Getter
    protected YdeReport report;

    @Override
    public boolean hasError() {
        return errorCode != null;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String getCode() {
        return errorCode;
    }

    protected void validate() {
        if(questions.size() != answers.size()){
            errorCode = "404";
            errorMessage = "答案数量不一致";
            return;
        }

    }

    @Override
    public ReportBuilder addAnswers(List<YdeAnswer> answers) {
        this.answers = answers;
        return this;
    }

    @Override
    public ReportBuilder addQuestions(List<YdeQuestion> questions) {
        this.questions = questions;
        questionIdMap = questions.stream().collect(Collectors.toMap(YdeQuestion::getId, Function.identity()));
        return this;
    }

    protected void calcTotalScore(){
        //总分
        totalScore = questions.stream().collect(Collectors.summingInt(q ->
                q.getPoints()
        ));
    }

    public void build() {
        validate();
        preBuild();
        calcTotalScore();
        buildReport();
    }

    protected abstract void preBuild();

    protected abstract void buildReport();
}
