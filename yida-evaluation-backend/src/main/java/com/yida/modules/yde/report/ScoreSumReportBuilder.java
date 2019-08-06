package com.yida.modules.yde.report;

import com.yida.modules.yde.entity.YdeReport;
import com.yida.modules.yde.entity.YdeScoreRule;
import com.yida.modules.yde.service.YdeScoreRuleService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class ScoreSumReportBuilder extends AbstractReportBuilder {
    private Set<YdeScoreRule> rules = new TreeSet<>();
    private YdeScoreRuleService ydeScoreRuleService;

    public ScoreSumReportBuilder(YdeScoreRuleService ydeScoreRuleService) {
        this.ydeScoreRuleService = ydeScoreRuleService;
    }

    @Override
    protected void preBuild() {
        YdeScoreRule where = new YdeScoreRule();
        where.setRuleId(Long.valueOf(rule.getId()));
        rules.addAll(ydeScoreRuleService.findList(where));
    }

    @Override
    protected void buildReport() {
        report = new YdeReport();
        report.setTitle(evaluation.getName());  //标题

        //累计分数
        Integer score = answers.stream().collect(Collectors.summingInt(a ->
                questionIdMap.get(String.valueOf(a.getQuestionId()))
                        .getPoints(a.getAnswer())
        ));

        report.setScores(new ArrayList<>(rules));

        report.setScore(score); //累计得分

        report.setTotalScore(totalScore);   //总分

        report.setReportType(rule.getReportType()); //结果展现

        report.setReportContent(rule.getResult()); //分析结果



        YdeScoreRule scoreRule = findMatchedScoreRule(score);
        if (scoreRule != null) {
            report.setTypeContent(scoreRule.getContent());
            String scoreRange = "<p><span>" + scoreRule.getScoreFrom() + " ～ " + scoreRule.getScoreTo() + ": </span></p>";
            String typeContent = "<p><span>" + scoreRule.getContent() + "</span></p>";
            String ruleResult = "<p><span>" + rule.getResult() + "</span></p>";
            String reportContent = "<div>" + scoreRange + typeContent + ruleResult + "</div>";
            report.setReportContent(reportContent);
        }
    }

    private YdeScoreRule findMatchedScoreRule(Integer score) {
        Optional<YdeScoreRule> optional = rules.stream().filter(r -> r.inRange(score)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
