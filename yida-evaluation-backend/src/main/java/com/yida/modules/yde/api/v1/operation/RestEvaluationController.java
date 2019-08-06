/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.entity.Page;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.ApiPageable;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.service.YdeEvaluationService;
import com.yida.modules.yde.service.YdeModuleService;
import com.yida.modules.yde.service.YdePartService;
import com.yida.modules.yde.service.YdeQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 益达人才测评
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/evaluations")
@Api(value = "益达人才测评", description = "测评")
public class RestEvaluationController extends AbstractYdeController {

    @Autowired
    private YdeEvaluationService ydeEvaluationService;

    @Autowired
    private YdeQuestionService ydeQuestionService;

    @Autowired
    private YdeModuleService ydeModuleService;

    @Autowired
    private YdePartService ydePartService;

    @Autowired
    private UserSession userSession;

    /**
     * 测评列表
     */
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "测评列表", notes = "列表所有测评对象", httpMethod = "GET")
    @DefaultApiResponse
    public @ResponseBody
    RestResponse<YdeEvaluation> list(
            @ApiParam(value = "访问令牌", required = true, example = "<token>") @RequestParam String token
    ) {
        YdeEvaluation evaluationWhere = new YdeEvaluation();
        evaluationWhere.setEnabled("1");
        List<YdeEvaluation> evaluations = ydeEvaluationService.findList(evaluationWhere);
        evaluations.stream().filter(e -> {
            if ("1".equals(e.getLimitedOnce())) {
                return getUserEvaluatedCount(Long.valueOf(userSession.getUser(token).getId()), Long.valueOf(e.getId())) == 0l;
            }
            return true;
        }).collect(Collectors.toList());


        YdeModule where = new YdeModule();
        List<YdeModule> modules = ydeModuleService.findList(where);
        Map<String, String> idModuleNameMap = modules.stream().collect(Collectors.toMap(YdeModule::getId, YdeModule::getName));
        evaluations.stream().forEach(e -> e.setModuleName(idModuleNameMap.get(String.valueOf(e.getModuleId()))));
        return new RestResponse(evaluations);
    }

    /**
     * 查询测评
     */
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询测评", notes = "获取单个测评信息", httpMethod = "GET")
    @DefaultApiResponse
    public @ResponseBody
    RestResponse<YdeEvaluation> get(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {

        YdeEvaluation evaluation = ydeEvaluationService.get(new YdeEvaluation(id));
        if ("1".equals(evaluation.getEnabled())) {
            if ("1".equals(evaluation.getLimitedOnce())) {
                if (getUserEvaluatedCount(Long.valueOf(userSession.getUser(token).getId()), Long.valueOf(evaluation.getId())) == 0l) {
                    return new RestResponse(evaluation);
                } else {
                    return new RestResponse(new ArrayList(), "404", "只能测评一次");
                }
            }
            return new RestResponse(evaluation);
        }
        return new RestResponse(new ArrayList(), "404", "没有可用的测评");
    }

    /**
     * 查询某个测评的用户记录
     */
    @ResponseBody
    @RequestMapping(value = "{id}/records", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询某个测评的用户记录", notes = "获取单个测评用户记录信息", httpMethod = "GET")
    @DefaultApiResponse
    @ApiPageable
    public RestResponse<YdeEvaluationRecord> records(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        YdeEvaluationRecord evaluationRecord = new YdeEvaluationRecord();
        evaluationRecord.setEvaluationId(Long.valueOf(id));
        evaluationRecord.setUserId(Long.valueOf(userSession.getUser(token).getId()));
        evaluationRecord.setPage(new Page(request, response));
        Page<YdeEvaluationRecord> page = ydeEvaluationRecordService.findPage(evaluationRecord);

        return new RestResponse(page.getList());
    }

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(value = "{id}/questions", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "列表和测评关联的题目", notes = "列表和测评关联的题目", httpMethod = "GET")
    @DefaultApiResponse
    @ApiPageable
    public RestResponse<YdeQuestion> questions(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        YdeQuestion question = new YdeQuestion();
        question.setEvaluationId(Long.valueOf(id));
        question.setPage(new Page(request, response));
        Page<YdeQuestion> page = ydeQuestionService.findPage(question);

        return new RestResponse(page.getList());
    }

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(value = "{id}/reports", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据测评查找对应报告列表", notes = "根据测评查找对应报告列表", httpMethod = "GET")
    @DefaultApiResponse
    @ApiPageable
    public RestResponse<YdeReport> reports(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        YdeEvaluationRecord evaluationRecord = new YdeEvaluationRecord();
        evaluationRecord.setEvaluationId(Long.valueOf(id));
        YdeUser user = userSession.getUser(token);
        evaluationRecord.setUserId(Long.valueOf(user.getId()));
        List<YdeReport> reports = ydeEvaluationRecordService.findReportsByEvaluationRecord(evaluationRecord);

        return new RestResponse(reports);
    }

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(value = "{id}/parts", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "根据测评查找对应部分描述列表", notes = "根据测评查找对应部分描述列表", httpMethod = "GET")
    @DefaultApiResponse
    @ApiPageable
    public RestResponse<YdeReport> parts(
            @ApiParam(value = "测评ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token,
            HttpServletRequest request, HttpServletResponse response
    ) {
        YdePart where = new YdePart();
        where.setEvaluationId(Long.valueOf(id));
        List<YdePart> reports = ydePartService.findList(where);

        return new RestResponse(reports);
    }
}