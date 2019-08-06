package com.yida.modules.yde.report;

import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.entity.YdeRule;
import com.yida.modules.yde.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportBuilderFactory {
    @Autowired
    private YdeRuleService ydeRuleService;

    @Autowired
    private YdeEvaluationService ydeEvaluationService;

    @Autowired
    private YdeScoreRuleService ydeScoreRuleService;

    @Autowired
    private YdeTypeRuleService ydeTypeRuleService;

    @Autowired
    private YdeModuleService ydeModuleService;

    @Autowired
    private YdeTypeGroupAnalysisService ydeTypeGroupAnalysisService;

    public ReportBuilder getProcessor(Long evaluationId) {
        AbstractReportBuilder builder = null;

        YdeRule where = new YdeRule();
        where.setEvaluationId(evaluationId);
        List<YdeRule> rules = ydeRuleService.findList(where);
        YdeRule rule = rules.isEmpty() ? null : rules.get(0);

        YdeEvaluation evaluation = ydeEvaluationService.get(String.valueOf(evaluationId));
        YdeModule module = ydeModuleService.get(String.valueOf(evaluation.getModuleId()));

        if (rule == null) {
            builder = new DefaultReportBuilder();
            builder.errorCode = "404";
            builder.errorMessage = "评分规则未定义";
            return builder;
        }

        if (YdeRule.RULE_TYPE_GROUP_SUM.equals(rule.getRuleType())) {
            builder = new TypeSumReportBuilder(ydeTypeRuleService, ydeTypeGroupAnalysisService);
        } else if (YdeRule.RULE_TYPE_SCORE_SUM.equals(rule.getRuleType())) {
            builder = new ScoreSumReportBuilder(ydeScoreRuleService);
        }
        builder.setYdeRuleService(ydeRuleService);
        builder.setEvaluation(evaluation);
        builder.setModule(module);
        builder.setRule(rule);
        return builder;
    }
}
