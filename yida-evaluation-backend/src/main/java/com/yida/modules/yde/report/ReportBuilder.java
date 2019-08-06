package com.yida.modules.yde.report;

import com.yida.modules.yde.api.v1.model.YdeAnswer;
import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.entity.YdeQuestion;
import com.yida.modules.yde.entity.YdeReport;

import java.util.List;

public interface ReportBuilder {

    ReportBuilder addAnswers(List<YdeAnswer> answers);

    ReportBuilder addQuestions(List<YdeQuestion> questions);

    boolean hasError();

    String getCode();

    String getErrorMessage();

    YdeReport getReport();

    YdeEvaluation getEvaluation();

    YdeModule getModule();

    void build();
}
