/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.HtmlUtils;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.api.v1.model.YdeAnswer;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.report.ReportBuilder;
import com.yida.modules.yde.report.ReportBuilderFactory;
import com.yida.modules.yde.service.*;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 益达人才测评模块
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/reports")
@Api(value = "益达人才测评", description = "测评结果接口")
@Slf4j
public class RestReportController extends BaseController {

    @Autowired
    private YdeReportService ydeReportService;

    @Autowired
    private YdeQuestionService ydeQuestionService;

    @Autowired
    private YdeRuleService ydeRuleService;

    @Autowired
    private YdeScoreRuleService ydeScoreRuleService;

    @Autowired
    private ReportBuilderFactory answerProcessorFactory;

    @Autowired
    private YdeEvaluationRecordService evaluationRecordService;

    @Autowired
    private UserSession userSession;

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取所有最新测评结果")
    @DefaultApiResponse
    public RestResponse<YdeReport> reports(
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        String userId = userSession.getUser(token).getId();
        List<YdeReport> reports = evaluationRecordService.findLastReports(userId);

        if(reports.isEmpty()){
            return newResponse("404", "找不到此记录");
        }

        for(YdeReport report: reports){
            updateScores(report);
            report.setReportContent(HtmlUtils.htmlEscape(report.getReportContent()));
        }

        return new RestResponse(reports);
    }

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "获取某个测评结果")
    @DefaultApiResponse
    public RestResponse<YdeReport> getResult(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        List<YdeReport> records = new LinkedList<YdeReport>();

        YdeReport report = ydeReportService.get(id);

        updateScores(report);

        if(report == null){
            return newResponse("404", "找不到此记录");
        }
        records.add(report);
        return new RestResponse(records);
    }

    protected void updateScores(YdeReport report) {
        YdeRule rule = ydeRuleService.get(String.valueOf(report.getYdeRule().getId()));
        if(YdeRule.RULE_TYPE_SCORE_SUM.equals(rule.getRuleType())){
            YdeScoreRule where = new YdeScoreRule();
            where.setRuleId(Long.valueOf(rule.getId()));
            report.setScores(ydeScoreRuleService.findList(where));
        }
    }

    /**
     *
     */
    @ResponseBody()
    @RequestMapping(value = "analysis", method = RequestMethod.POST)
    @ApiOperation(value = "分析测评")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(
            @ApiResponse(code = 201, message = "成功创建分析报告")
    )
    public RestResponse<YdeReport> analysis(
            @ApiParam(value = "题目回答列表", required = true, allowMultiple = true) @RequestBody List<YdeAnswer> answers,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        log.info("Receive analysis request: {}", JsonMapper.toJson(answers));
        Integer answerCount = answers.size();
        if(answerCount == 0){
            return newResponse("404", "答案列表为空");
        }

        YdeAnswer answer = answers.get(0);
        YdeQuestion firstQuestion = ydeQuestionService.get(String.valueOf(answer.getQuestionId()));

        YdeQuestion where = new YdeQuestion();
        where.setEvaluationId(firstQuestion.getEvaluationId());
        List<YdeQuestion> allQuestions = ydeQuestionService.findListWithOption(where);

        ReportBuilder builder = answerProcessorFactory.getProcessor(firstQuestion.getEvaluationId());
        builder.addAnswers(answers).addQuestions(allQuestions).build();

        if(builder.hasError()){
            return newResponse(builder.getCode(), builder.getErrorMessage());
        }

        YdeReport report = builder.getReport();
        ydeReportService.save(report);

        log.info("Report generated, report id: {}", report.getId());

        YdeEvaluationRecord record = new YdeEvaluationRecord();

        YdeUser user = userSession.getUser(token);
        record.setUserId(Long.valueOf(user.getId()));
        record.setEvaluationId(record.getEvaluationId());
        record.setEvaluateTime(new Date());
        record.setEvaluationId(builder.getEvaluation().idLongValue());
        record.setEvaluationName(builder.getEvaluation().getName());
        record.setIsLastEvaluation(Boolean.TRUE);
        record.setModuleName(builder.getModule().getName());
        record.setReportId(report.idLongValue());
        evaluationRecordService.resetLastEvaluation(user.idLongValue(), firstQuestion.getEvaluationId());
        evaluationRecordService.resetLastEvaluation(user.idLongValue(), firstQuestion.getEvaluationId());
        evaluationRecordService.insert(record);

        log.info("Evaluation record created, record id: {}", record.getId());
        return new RestResponse(report);
    }

    private RestResponse<YdeReport> newResponse(String code, String message) {
        RestResponse response = new RestResponse((YdeReport)null);
        response.setCode(code);
        response.setMsg(message);
        return response;
    }
}