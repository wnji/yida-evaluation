/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.HtmlUtils;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.entity.*;
import com.yida.modules.yde.service.*;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 测评设置Controller
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeEvaluation")
public class YdeEvaluationController extends BaseController {

    @Autowired
    private YdeEvaluationService ydeEvaluationService;

    @Autowired
    private YdeModuleService ydeModuleService;

    @Autowired
    private YdeQuestionService ydeQuestionService;

    @Autowired
    private YdeRuleService ydeRuleService;

    @Autowired
    private YdeScoreRuleService ydeScoreRuleService;

    @Autowired
    private YdeTypeRuleService ydeTypeRuleService;

    @Autowired
    private YdeTypeGroupAnalysisService ydeTypeGroupAnalysisService;

    @Autowired
    private YdePartService ydePartService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public YdeEvaluation get(String id, boolean isNewRecord) {
        return ydeEvaluationService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("yde:ydeEvaluation:view")
    @RequestMapping(value = {"list", ""})
    public String list(YdeEvaluation ydeEvaluation, Model model) {
        model.addAttribute("ydeEvaluation", ydeEvaluation);
        model.addAttribute("modules", ydeModuleService.findList(new YdeModule()));
        return "modules/yde/ydeEvaluationList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("yde:ydeEvaluation:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<YdeEvaluation> listData(YdeEvaluation ydeEvaluation, HttpServletRequest request, HttpServletResponse response) {
        ydeEvaluation.setPage(new Page<>(request, response));
        Page<YdeEvaluation> page = ydeEvaluationService.findPage(ydeEvaluation);
        for (YdeEvaluation evaluation : page.getList()) {
            evaluation.setModuleName(evaluation.getModule().getName());
        }
        return page;
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("yde:ydeEvaluation:view")
    @RequestMapping(value = "questions", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String questions(String evaluationId) {
        if (StringUtils.isEmpty(evaluationId)) {
            return JsonMapper.toJson(ListUtils.newArrayList());
        }
        YdeQuestion questionWhere = new YdeQuestion();
        questionWhere.setEvaluationId(Long.valueOf(evaluationId));
        List<YdeQuestion> questions = ydeQuestionService.findListWithOption(questionWhere);
        return JsonMapper.toJson(questions);
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("yde:ydeEvaluation:view")
    @RequestMapping(value = "analysis", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String analysis(String evaluationId) {
        if (StringUtils.isEmpty(evaluationId)) {
            return JsonMapper.toJson(ListUtils.newArrayList());
        }
        YdeRule rule = ydeEvaluationService.get(evaluationId).getRule();
        if(rule== null){
            return JsonMapper.toJson(ListUtils.newArrayList());
        }
        YdeTypeGroupAnalysis analysisWhere = new YdeTypeGroupAnalysis();
        analysisWhere.setRuleId(rule.idLongValue());
        List<YdeTypeGroupAnalysis> analysis = ydeTypeGroupAnalysisService.findList(analysisWhere);
        return JsonMapper.toJson(analysis);
    }

    /**
     * 查询分析数据
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @RequestMapping(value = "getAnalysis", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getAnalysis(String analysisId) {
        if (StringUtils.isEmpty(analysisId)) {
            return JsonMapper.toJson(ListUtils.newArrayList());
        }
        YdeTypeGroupAnalysis analysis = ydeTypeGroupAnalysisService.get(analysisId);
        return JsonMapper.toJson(analysis);
    }

    /**
     * 保存分析数据
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @PostMapping(value = "saveAnalysis")
    @ResponseBody
    public String saveAnalysis(@Validated YdeTypeGroupAnalysis ydeTypeGroupAnalysis, HttpServletRequest request) {
        ydeTypeGroupAnalysisService.save(ydeTypeGroupAnalysis);
        return renderResult(Global.TRUE, text("保存分析成功！"), ydeTypeGroupAnalysis);
    }

    /**
     * 保存分析数据
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @PostMapping(value = "deleteAnalysis")
    @ResponseBody
    public String deleteAnalysis(String id) {
        ydeTypeGroupAnalysisService.delete(ydeTypeGroupAnalysisService.get(id));
        return renderResult(Global.TRUE, text("删除分析成功！"));
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("yde:ydeEvaluation:view")
    @RequestMapping(value = "form")
    public String form(YdeEvaluation ydeEvaluation, String step, Model model) throws JsonProcessingException {
        model.addAttribute("ydeEvaluation", ydeEvaluation);
        List<YdeModule> modules = ydeModuleService.findList(new YdeModule());
        model.addAttribute("modules", modules);

        if (!ydeEvaluation.isNewRecord()) {
            YdeQuestion questionWhere = new YdeQuestion();
            questionWhere.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
            List<YdeQuestion> questions = ydeQuestionService.findListWithOption(questionWhere);

            if ("1".equals(ydeEvaluation.getPart())) {
                YdePart partWhere = new YdePart();
                partWhere.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
                List<YdePart> parts = ydePartService.findList(partWhere);
                parts.forEach(p -> p.setContent(StringEscapeUtils.escapeJson(p.getContent())));
                model.addAttribute("partsJson", JsonMapper.toJson(parts));
            }

            YdeRule rule = ydeEvaluation.getRule();

            if (step == null || "1".equals(step)) {
                if (questions.isEmpty()) {
                    step = "1";
                } else {
                    if (rule == null) {
                        step = "2";
                        //第二步，题目设置
                    } else {
                        step = "3";
                        //第三步规则设置
                        model.addAttribute("ruleId", rule.getId());
                        model.addAttribute("ruleType", rule.getRuleType());
                        model.addAttribute("reportType", rule.getReportType());
                        model.addAttribute("statisticsMethod", rule.getStatisticsMethod());
                        ydeEvaluation.setRuleType(rule.getRuleType());
                        ydeEvaluation.setReportType(rule.getReportType());
                        ydeEvaluation.setResult(HtmlUtils.replaceImgMaxWidth(rule.getResult()));
                        ydeEvaluation.setStatisticsMethod(rule.getStatisticsMethod());
                        ydeEvaluation.setPointsSource(rule.getPointsSource());

                        if (YdeRule.RULE_TYPE_SCORE_SUM == rule.getRuleType()) {
                            YdeScoreRule where = new YdeScoreRule();
                            where.setRuleId(rule.idLongValue());
                            List<YdeScoreRule> scoreRules = ydeScoreRuleService.findList(where);
                            model.addAttribute("rulesJson", JsonMapper.toJson(scoreRules));
                        } else if (YdeRule.RULE_TYPE_GROUP_SUM == rule.getRuleType()) {
                            YdeTypeRule where = new YdeTypeRule();
                            where.setRuleId(rule.idLongValue());
                            List<YdeTypeRule> typeRules = ydeTypeRuleService.findList(where);
                            for (YdeTypeRule typeRule : typeRules) {
                                String[] questionIds = typeRule.getQuestionIds().split(",");
                                List<String> questionIdList = Arrays.asList(questionIds).stream().collect(Collectors.toList());
                                List<String> questionNos = ydeQuestionService.convertIdToNo(rule.getEvaluationId(), questionIdList);
                                typeRule.setQuestionNos(questionNos.stream().collect(Collectors.joining(",")));
                            }
                            model.addAttribute("rulesJson", JsonMapper.toJson(typeRules));

                        }
                    }
                }
            }
        } else {
            step = "1";
        }
        defaultIfBlank(model, "ruleType");
        defaultIfBlank(model, "ruleId");
        defaultIfBlank(model, "reportType");
        defaultIfBlank(model, "rulesJson");
        defaultIfBlank(model, "partsJson");

        model.addAttribute("step", step);
        return "modules/yde/ydeEvaluationForm";
    }

    protected void defaultIfBlank(Model model, String ruleType) {
        if (model.asMap().get(ruleType) == null) {
            model.addAttribute(ruleType, "");
        }
    }

    /**
     * 保存测评设置
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated YdeEvaluation ydeEvaluation, HttpServletRequest request) {
        ydeEvaluation.setBackground(HtmlUtils.replaceImgMaxWidth(ydeEvaluation.getBackground()));
        ydeEvaluationService.save(ydeEvaluation);

        Map<String, String[]> params = request.getParameterMap();
        if (params.get("part") != null && "1".equals(getSingleParam(params, "part"))) {
            String partsJson = getSingleParam(params, "partsJson");
            if (StringUtils.isNotBlank(partsJson)) {
                handlePartsJson(ydeEvaluation, partsJson);
            }
        } else if (!ydeEvaluation.isNewRecord()) {
            YdePart where = new YdePart();
            where.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
            List<YdePart> partsFromDb = ydePartService.findList(where);
            partsFromDb.forEach(p -> ydePartService.delete(p));
        }


        if (params.get("ruleType") != null && params.get("ruleType").length > 0) {
            YdeRule rule = ydeEvaluation.getRule();
            if (rule == null) {
                rule = new YdeRule();
                rule.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
                ydeEvaluation.setRule(rule);
            }

            Integer ruleType = getSingleParamInt(params, "ruleType");
            rule.setRuleType(ruleType);

            rule.setReportType(getSingleParamInt(params, "reportType"));
            rule.setStatisticsMethod(getSingleParamInt(params, "statisticsMethod"));
            rule.setPointsSource(getSingleParamInt(params, "pointsSource"));


            if (YdeRule.RULE_TYPE_SCORE_SUM.equals(ruleType)) {
                rule.setResult(getSingleParam(params, "result"));
                ydeRuleService.save(rule);

                String ruleJson = getSingleParam(params, "ruleJson");
                if (StringUtils.isNotBlank(rule.getId()) && StringUtils.isNotBlank(ruleJson)) {
                    handleScoreJson(rule, ruleJson);
                }
            } else if (YdeRule.RULE_TYPE_GROUP_SUM.equals(ruleType)) {
                ydeRuleService.save(rule);
                String ruleJson = getSingleParam(params, "ruleJson");
                if (StringUtils.isNotBlank(rule.getId()) && StringUtils.isNotEmpty(ruleJson)) {
                    handleTypeJson(rule, ruleJson);
                }
            }

        }

        Map retData = new HashMap();
        retData.put("id", ydeEvaluation.getId());
        return renderResult(Global.TRUE, text("保存测评设置成功！"), retData);
    }

    private void handlePartsJson(YdeEvaluation ydeEvaluation, String partsJson) {
        List<Map<String, String>> partsList = JsonMapper.fromJson(partsJson, List.class);
        List<YdePart> partsToSave = new ArrayList<>();
        YdePart where = new YdePart();
        where.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
        List<YdePart> partsToDelete = ydePartService.findList(where);

        for (int i = 0; i < partsList.size(); i++) {
            Map<String, String> partItem = partsList.get(i);
            String partId = partItem.get("partId");
            String content = partItem.get("part");
            YdePart part;
            if (StringUtils.isNotBlank(partId)) {
                part = ydePartService.get(partId);
                if (part != null) {
                    partsToSave.add(part);
                    Optional<YdePart> optional = partsToDelete.stream().filter(p -> p.getId().equals(partId)).findFirst();
                    if (optional.isPresent()) {
                        partsToDelete.remove(optional.get());
                    }
                }
            } else {
                part = new YdePart();
                part.setEvaluationId(Long.valueOf(ydeEvaluation.getId()));
                partsToSave.add(part);
            }
            part.setContent(HtmlUtils.replaceImgMaxWidth(content));
            part.setSection("第" + (i + 1) + "部分");
        }
        if (!partsToDelete.isEmpty()) {
            partsToDelete.forEach(p -> ydePartService.delete(p));
        }
        if (!partsToSave.isEmpty()) {
            partsToSave.forEach(p -> ydePartService.save(p));
        }
    }

    protected void handleTypeJson(YdeRule rule, String ruleJson) {
        List<Map<String, String>> ruleList = JsonMapper.fromJson(ruleJson, List.class);
        YdeTypeRule typeRuleWhere = new YdeTypeRule();
        typeRuleWhere.setRuleId(Long.valueOf(rule.getId()));
        List<YdeTypeRule> rulesInDb = ydeTypeRuleService.findList(typeRuleWhere);
        List<YdeTypeRule> rulesToSave = ListUtils.newArrayList();

        YdeTypeGroupAnalysis analysisWhere = new YdeTypeGroupAnalysis();
        analysisWhere.setRuleId(Long.valueOf(rule.getId()));


        Integer currentGrpNo = -1;
        int optionIdx = (int) 'A';
        for (Map<String, String> ruleItem : ruleList) {
            String ruleId = ruleItem.get("ruleId");
            String groupNo = ruleItem.get("groupNo");
            String type = ruleItem.get("type");
            String questionNo = ruleItem.get("questionNo");

            YdeTypeRule typeRule = StringUtils.isBlank(ruleId) ? null : ydeTypeRuleService.get(ruleId);
            if (typeRule == null) {
                typeRule = new YdeTypeRule();
            }

            typeRule.setGroupNo(Integer.valueOf(groupNo));
            if (currentGrpNo == typeRule.getGroupNo()) {
                typeRule.setOptionName(Character.toString((char) optionIdx++));
            } else {
                currentGrpNo = typeRule.getGroupNo();
                optionIdx = (int) 'A';
                typeRule.setOptionName(Character.toString((char) optionIdx++));
            }

            typeRule.setType(type);
            typeRule.setRuleId(Long.valueOf(rule.getId()));

            String[] questionNos = questionNo.split(",");
            List<String> questionNoList = Arrays.asList(questionNos).stream().map(q -> q.trim()).collect(Collectors.toList());
            List<String> questionIds = ydeQuestionService.convertNoToId(rule.getEvaluationId(), questionNoList);
            typeRule.setQuestionIds(questionIds.stream().collect(Collectors.joining(",")));
            rulesToSave.add(typeRule);
        }
        if (!rulesInDb.isEmpty()) {
            rulesInDb.stream().filter(r ->
                    !rulesToSave.stream().filter(toSave -> r.getId().equals(toSave.getId())).findFirst().isPresent()).forEach(r ->
                    ydeTypeRuleService.delete(r));
        }
        if (!rulesToSave.isEmpty()) {
            rulesToSave.forEach(r -> ydeTypeRuleService.save(r));
        }
    }

    protected void handleScoreJson(YdeRule rule, String scoreJson) {
        List<Map<String, String>> scoreRules = JsonMapper.fromJson(scoreJson, List.class);
        YdeScoreRule scoreRuleWhere = new YdeScoreRule();
        scoreRuleWhere.setRuleId(Long.valueOf(rule.getId()));
        List<YdeScoreRule> rulesInDb = ydeScoreRuleService.findList(scoreRuleWhere);
        List<YdeScoreRule> rulesToSave = ListUtils.newArrayList();
        for (Map<String, String> scoreRuleMap : scoreRules) {
            Integer from = Integer.valueOf(scoreRuleMap.get("from"));
            Integer to = Integer.valueOf(scoreRuleMap.get("to"));
            YdeScoreRule ydeScoreRule = getRule(rulesInDb, from, to);
            if (ydeScoreRule == null) {
                ydeScoreRule = new YdeScoreRule();
                ydeScoreRule.setRuleId(Long.valueOf(rule.getId()));
                ydeScoreRule.setScoreFrom(from);
                ydeScoreRule.setScoreTo(to);
            }
            String content = scoreRuleMap.get("content");
            content = StringUtils.isNotBlank(content) ? content.trim() : "";
            ydeScoreRule.setContent(content);
            rulesToSave.add(ydeScoreRule);
        }
        //数据库有但保存列表中没有但数据要删除
        if (!rulesInDb.isEmpty()) {
            rulesInDb.stream().filter(r -> !rulesToSave.contains(r)).forEach(r ->
                    ydeScoreRuleService.delete(r));
        }
        if (!rulesToSave.isEmpty()) {
            rulesToSave.forEach(r -> ydeScoreRuleService.save(r));
        }
    }

    private YdeScoreRule getRule(List<YdeScoreRule> rules, Integer from, Integer to) {
        Optional<YdeScoreRule> optional = rules.stream().filter(r -> r.getScoreFrom().equals(from) && r.getScoreTo().equals(to)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    private String getSingleParam(Map<String, String[]> params, String key) {
        return params.get(key) != null ? params.get(key)[0] : "";
    }

    private Integer getSingleParamInt(Map<String, String[]> params, String key) {
        String strVal = params.get(key) != null ? params.get(key)[0] : "";
        return StringUtils.isBlank(strVal) ? 0 : Integer.valueOf(strVal);
    }

    /**
     * 删除测评设置
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(YdeEvaluation ydeEvaluation) {
        ydeEvaluationService.delete(ydeEvaluation);
        return renderResult(Global.TRUE, text("删除测评设置成功！"));
    }


    /**
     * 批量删除测评设置
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @RequestMapping(value = "batchDelete")
    @ResponseBody
    public String batchDelete(String[] ids) {
        for (String id : ids) {
            YdeEvaluation ydeEvaluation = new YdeEvaluation();
            ydeEvaluation.setId(id);
            ydeEvaluationService.delete(ydeEvaluation);
        }
        return renderResult(Global.TRUE, text("批量删除测评设置成功！"), ids);
    }

    /**
     * 批量激活测评设置
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @RequestMapping(value = "enable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String enable(String[] ids) {
        for (String id : ids) {
            YdeEvaluation ydeEvaluation = ydeEvaluationService.get(id);
            ydeEvaluation.setEnabled("1");
            ydeEvaluationService.save(ydeEvaluation);
        }
        return renderResult(Global.TRUE, text("激活测评成功！"));
    }

    /**
     * 批量冻结测评设置
     */
    @RequiresPermissions("yde:ydeEvaluation:edit")
    @RequestMapping(value = "disable", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String disable(String[] ids) {
        for (String id : ids) {
            YdeEvaluation ydeEvaluation = ydeEvaluationService.get(id);
            ydeEvaluation.setEnabled("0");
            ydeEvaluationService.save(ydeEvaluation);
        }
        return renderResult(Global.TRUE, text("冻结测评成功！"));
    }
}