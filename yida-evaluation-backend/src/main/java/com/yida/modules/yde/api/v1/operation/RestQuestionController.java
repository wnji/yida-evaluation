/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.entity.YdeOption;
import com.yida.modules.yde.entity.YdeQuestion;
import com.yida.modules.yde.service.YdeOptionService;
import com.yida.modules.yde.service.YdeQuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 益达人才测评模块
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/questions")
@Api(value = "益达人才测评题目", description = "题目接口")
public class RestQuestionController extends BaseController {

    @Autowired
    private YdeQuestionService ydeQuestionService;

    @Autowired
    private YdeOptionService ydeOptionService;

    /**
     * 查询测评
     */
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询题目", notes = "查询题目", httpMethod = "GET")
    @DefaultApiResponse
    public RestResponse<YdeQuestion> get(
            @ApiParam(value = "题目ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        YdeQuestion question = ydeQuestionService.get(new YdeQuestion(id));
        if (question != null && question.getId() != null) {
            YdeOption where = new YdeOption();
            where.setQuestionId(Long.valueOf(question.getId()));
            List<YdeOption> options = ydeOptionService.findList(where);
            if (YdeQuestion.OPTION_TYPE_FILLING == question.getOptionType()) {
                YdeOption fillingOption = new YdeOption();
                fillingOption.setOptionType(YdeQuestion.OPTION_TYPE_FILLING);
                options.add(fillingOption);
            }
            question.setOptions(options);
        }
        return new RestResponse(question);
    }
}