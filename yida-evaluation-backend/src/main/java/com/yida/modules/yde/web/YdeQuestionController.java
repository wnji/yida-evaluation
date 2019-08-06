/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.web;

import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.config.Global;
import com.jeesite.common.entity.Page;
import com.jeesite.common.mapper.JsonMapper;
import com.jeesite.common.utils.HtmlUtils;
import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.entity.YdeOption;
import com.yida.modules.yde.entity.YdePart;
import com.yida.modules.yde.entity.YdeQuestion;
import com.yida.modules.yde.service.YdeEvaluationService;
import com.yida.modules.yde.service.YdeOptionService;
import com.yida.modules.yde.service.YdePartService;
import com.yida.modules.yde.service.YdeQuestionService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 题目设置Controller
 *
 * @author Kevin
 * @version 2019-05-02
 */
@Controller
@RequestMapping(value = "${adminPath}/yde/ydeQuestion")
public class YdeQuestionController extends BaseController {

    @Autowired
    private YdeQuestionService ydeQuestionService;

    @Autowired
    private YdeOptionService ydeOptionService;

    @Autowired
    private YdeEvaluationService ydeEvaluationService;

    @Autowired
    private YdePartService ydePartService;

    /**
     * 获取数据
     */
    @ModelAttribute
    public YdeQuestion get(String id, boolean isNewRecord) {
        return ydeQuestionService.get(id, isNewRecord);
    }

    /**
     * 查询列表
     */
    @RequiresPermissions("yde:ydeQuestion:view")
    @RequestMapping(value = {"list", ""})
    public String list(YdeQuestion ydeQuestion, Model model) {
        model.addAttribute("ydeQuestion", ydeQuestion);
        return "modules/yde/ydeQuestionList";
    }

    /**
     * 查询列表数据
     */
    @RequiresPermissions("yde:ydeQuestion:view")
    @RequestMapping(value = "listData")
    @ResponseBody
    public Page<YdeQuestion> listData(YdeQuestion ydeQuestion, HttpServletRequest request, HttpServletResponse response) {
        ydeQuestion.setPage(new Page<>(request, response));
        Page<YdeQuestion> page = ydeQuestionService.findPage(ydeQuestion);
        return page;
    }

    /**
     * 查看编辑表单
     */
    @RequiresPermissions("yde:ydeQuestion:view")
    @RequestMapping(value = "form")
    public String form(YdeQuestion ydeQuestion, String evaluationId, Model model) {
        YdeEvaluation ydeEvaluation = ydeEvaluationService.get(String.valueOf(ydeQuestion.getEvaluationId()));
        if (ydeQuestion.isNewRecord()) {
            ydeQuestion.setEvaluationId(Long.valueOf(evaluationId));
            long count = ydeQuestionService.findCount(ydeQuestion);
            ydeQuestion.setNo(count + 1);
            if (count > 0) {
                YdeQuestion where = new YdeQuestion();
                where.setEvaluationId(Long.valueOf(evaluationId));
                List<YdeQuestion> questions = ydeQuestionService.findList(where);
                YdeQuestion firstRecord = questions.get(0);
                ydeQuestion.setOptionType(firstRecord.getOptionType());
                ydeQuestion.setPoints(firstRecord.getPoints());
            }
            model.addAttribute("optionJson", JsonMapper.toJson(new ListUtils().newArrayList()));
            model.addAttribute("part", "0");
        } else {
            YdeOption where = new YdeOption();
            where.setQuestionId(Long.valueOf(ydeQuestion.getId()));
            List<YdeOption> options = ydeOptionService.findList(where);
            options.forEach(o -> o.copyPoints());
            model.addAttribute("optionJson", StringEscapeUtils.escapeJson(JsonMapper.toJson(options)));
        }
        model.addAttribute("part", ydeEvaluation.getPart());
        YdePart where = new YdePart();
        if (evaluationId == null && ydeQuestion.getEvaluationId() != null) {
            where.setEvaluationId(Long.valueOf(ydeQuestion.getEvaluationId()));
        } else if (evaluationId != null) {
            where.setEvaluationId(Long.valueOf(evaluationId));
        }
        model.addAttribute("parts", ydePartService.findList(where));
        model.addAttribute("ydeQuestion", ydeQuestion);
        model.addAttribute("optionType", String.valueOf(ydeQuestion.getOptionType()));
        return "modules/yde/ydeQuestionForm";
    }

    /**
     * 保存题目设置
     */
    @RequiresPermissions("yde:ydeQuestion:edit")
    @PostMapping(value = "save")
    @ResponseBody
    public String save(@Validated YdeQuestion ydeQuestion, HttpServletRequest request) {
        ydeQuestion.setQuestion(HtmlUtils.removeStyle(HtmlUtils.replaceImgMaxWidth(ydeQuestion.getQuestion())));

        YdeEvaluation evaluation = ydeEvaluationService.get(String.valueOf(ydeQuestion.getEvaluationId()));
        if (ydeQuestion.getPoints() == null) {
            ydeQuestion.setPoints(0);
        }
        ydeQuestionService.save(ydeQuestion);
        Integer maxPoints = ydeQuestion.getPoints();

        YdeOption where = new YdeOption();
        where.setQuestionId(Long.valueOf(ydeQuestion.getId()));
        List<YdeOption> optionsInDb = ydeOptionService.findList(where);

        List<YdeOption> optionsToSave = ListUtils.newArrayList();
        List<YdeOption> optionsToDelete = ListUtils.newArrayList();

        Map params = request.getParameterMap();
        if (YdeQuestion.OPTION_TYPE_SELECTION.equals(ydeQuestion.getOptionType()) ||
                YdeQuestion.OPTION_TYPE_ALLOCATION.equals(ydeQuestion.getOptionType())) {
            String optionsJson = ((String[]) params.get("options"))[0];
            List<Map<String, String>> options = JsonMapper.fromJson(optionsJson, List.class);

            for (Map<String, String> option : options) {
                String point = option.get("points");
                String name = option.get("name");
                String content = com.jeesite.common.utils.HtmlUtils.replaceImgMaxWidth(option.get("content"));
                YdeOption optionToSave = ObjectUtils.defaultIfNull(getOption(optionsInDb, name), new YdeOption());
                optionToSave.setOptionName(name);
                optionToSave.setOptionPoints(point == null ? 0 : Integer.valueOf(point));
                String optionHtml = StringEscapeUtils.unescapeHtml4(HtmlUtils.replaceImgMaxWidth(content));
                optionToSave.setContent(HtmlUtils.removeStyle(optionHtml));
                optionsToSave.add(optionToSave);
            }
            if (YdeQuestion.OPTION_TYPE_SELECTION.equals(ydeQuestion.getOptionType())) {
                maxPoints = options.stream().map(o -> Integer.valueOf(o.get("points")))
                        .max(Comparator.comparing(Integer::valueOf)).get();
            } else {
                maxPoints = ydeQuestion.getPoints();
            }

            if (options.size() > optionsToSave.size()) {
                optionsToDelete.addAll(optionsInDb.subList(optionsToSave.size() - 1, optionsInDb.size() - 1));
            }
            if (!optionsToDelete.isEmpty()) ydeOptionService.deleteBatch(optionsToDelete);
        } else if (YdeQuestion.OPTION_TYPE_YESNO.equals(ydeQuestion.getOptionType())) {
            YdeOption yes = ObjectUtils.defaultIfNull(getOption(optionsInDb, "是"), new YdeOption());
            yes.setOptionName("是");
            yes.setOptionPoints(getPoints(params, "optionPointsYes"));
            YdeOption no = ObjectUtils.defaultIfNull(getOption(optionsInDb, "否"), new YdeOption());
            no.setOptionName("否");
            no.setOptionPoints(getPoints(params, "optionPointsNo"));
            optionsToSave.add(yes);
            optionsToSave.add(no);
            maxPoints = Math.max(yes.getOptionPoints(), no.getOptionPoints());
        } else if (YdeQuestion.OPTION_TYPE_FILLING.equals(ydeQuestion.getOptionType())) {

        }

        optionsToSave.forEach(o -> {
            o.setQuestionId(Long.valueOf(ydeQuestion.getId()));
            o.setOptionType(ydeQuestion.getOptionType());
            ydeOptionService.save(o);
        });
        ydeQuestion.setPoints(maxPoints);
        ydeQuestionService.save(ydeQuestion);

        updateQuestionCount(evaluation);
        return renderResult(Global.TRUE, text("保存题目设置成功！"));
    }

    private Integer getPoints(Map params, String key) {
        String value = ((String[]) params.get(key))[0];
        return Integer.valueOf(StringUtils.defaultIfBlank(value, "0"));
    }

    private YdeOption getOption(List<YdeOption> options, String name) {
        Optional<YdeOption> optional = options.stream().filter(o -> name.equals(o.getOptionName())).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }

    /**
     * 删除题目设置
     */
    @RequiresPermissions("yde:ydeQuestion:edit")
    @RequestMapping(value = "delete")
    @ResponseBody
    public String delete(YdeQuestion ydeQuestion) {
        YdeEvaluation evaluation = ydeEvaluationService.get(String.valueOf(ydeQuestion.getEvaluationId()));
        Integer questionCount = ObjectUtils.defaultIfNull(evaluation.getQuestionCount(), 0);
        ydeQuestionService.delete(ydeQuestion);

        updateQuestionCount(evaluation);
        return renderResult(Global.TRUE, text("删除题目设置成功！"));
    }

    protected void updateQuestionCount(YdeEvaluation evaluation) {
        YdeQuestion questionWhere = new YdeQuestion();
        questionWhere.setEvaluationId(Long.valueOf(evaluation.getId()));
        evaluation.setQuestionCount(Long.valueOf(ydeQuestionService.findCount(questionWhere)).intValue());
        ydeEvaluationService.save(evaluation);
    }


    /**
     * 批量删除题目设置
     */
    @RequiresPermissions("yde:ydeQuestion:edit")
    @RequestMapping(value = "batchDelete")
    @ResponseBody
    public String batchDelete(String[] ids) {
        Long evaluationId = null;
        if (ids.length > 0) {
            YdeQuestion where = new YdeQuestion();
            where.setId(ids[0]);
            YdeQuestion question = ydeQuestionService.get(where);
            evaluationId = question.getEvaluationId();
        }

        for (String id : ids) {
            YdeQuestion ydeQuestion = new YdeQuestion();
            ydeQuestion.setId(id);

            ydeQuestionService.delete(ydeQuestion);
        }
        if (evaluationId != null) {
            updateQuestionCount(ydeEvaluationService.get(String.valueOf(evaluationId)));
        }
        return renderResult(Global.TRUE, text("批量删除题目设置成功！"), ids);
    }

}