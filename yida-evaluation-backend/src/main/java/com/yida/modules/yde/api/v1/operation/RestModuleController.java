/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.entity.YdeEvaluation;
import com.yida.modules.yde.entity.YdeModule;
import com.yida.modules.yde.service.YdeEvaluationService;
import com.yida.modules.yde.service.YdeModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 益达人才测评模块
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/modules")
@Api(value = "人才测评模块", description = "测评模块")
public class RestModuleController extends AbstractYdeController {

    @Autowired
    private YdeModuleService ydeModuleService;

    @Autowired
    private YdeEvaluationService ydeEvaluationService;

    @Autowired
    private UserSession userSession;
    /**
     * 测评列表
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "测评模块列表", notes = "测评模块列表", httpMethod = "GET")
    @DefaultApiResponse
    public RestResponse<YdeModule> list(
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        return new RestResponse(ydeModuleService.findList(new YdeModule()));
    }

    /**
     * 查询测评
     */
    @ResponseBody
    @RequestMapping(value = "{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "查询测评模块")
    @DefaultApiResponse
    public RestResponse<YdeModule> get(
            @ApiParam(value = "测评模块ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        return new RestResponse(ydeModuleService.get(new YdeModule(id)));
    }

    /**
     * 测评模块下的所有测评列表
     */
    @ResponseBody
    @RequestMapping(value = "{id}/evaluations", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "测评模块下的所有测评列表")
    @DefaultApiResponse
    public RestResponse<YdeEvaluation> evaluations(
            @ApiParam(value = "测评模块ID", example = "1", required = true) @PathVariable String id,
            @ApiParam(value = "访问令牌", example = "<token>", type = "query", required = true) @RequestParam String token
    ) {
        YdeEvaluation evaluation = new YdeEvaluation();
        evaluation.setModuleId(Long.valueOf(id));
        evaluation.setEnabled("1");

        List<YdeEvaluation> evaluations = ydeEvaluationService.findList(evaluation);
        evaluations = evaluations.stream().filter(e -> {
            if ("1".equals(e.getLimitedOnce())) {
                return getUserEvaluatedCount(Long.valueOf(userSession.getUser(token).getId()), Long.valueOf(e.getId())) == 0l;
            }
            return true;
        }).collect(Collectors.toList());
        return new RestResponse(evaluations);
    }


}