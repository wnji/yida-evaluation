/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 */
package com.yida.modules.yde.api.v1.operation;

import com.jeesite.common.web.BaseController;
import com.yida.modules.yde.api.session.UserSession;
import com.yida.modules.yde.api.v1.model.DefaultApiResponse;
import com.yida.modules.yde.api.v1.model.RestResponse;
import com.yida.modules.yde.dto.YdeEvaluationCount;
import com.yida.modules.yde.service.YdeEvaluationRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 益达人才测评
 *
 * @author Kevin
 * @version 2019-04-23
 */
@Controller
@RequestMapping(value = "${v1ApiPath}/records")
@Api(value = "益达人才测评", description = "测评记录")
public class RestRecordController extends BaseController {

    @Autowired
    private YdeEvaluationRecordService ydeEvaluationRecordService;

    @Autowired
    private UserSession userSession;

    /**
     * 查询测评结果列表
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ApiOperation(value = "列表所有测评次数", notes = "获取测评记录列表，我的>模块测评", httpMethod = "GET")
    @DefaultApiResponse
    public RestResponse<YdeEvaluationCount> records(@RequestParam String token) {
        String userId = userSession.getUser(token).getId();
        List<YdeEvaluationCount> evaluationRecords = ydeEvaluationRecordService.findEvaluationCountsByUserId(userId);
        return new RestResponse(evaluationRecords);
    }
}